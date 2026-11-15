package com.github.tvbox.osc.base

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.github.tvbox.osc.callback.EmptyCallback
import com.github.tvbox.osc.callback.LoadingCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import me.jessyan.autosize.internal.CustomAdapt

/** A View-backed section owned by a navigation screen. No FragmentManager is involved. */
abstract class ScreenSection : LifecycleOwner, ViewModelStoreOwner, ActivityResultCaller, CustomAdapt {
    private val registry = LifecycleRegistry(this)
    override val lifecycle: Lifecycle get() = registry
    override val viewModelStore = ViewModelStore()
    val viewLifecycleOwner: LifecycleOwner get() = this
    @JvmField protected var rootView: View? = null
    protected var mContext: Context? = null
    protected var mActivity: ScreenController? = null
    protected var currentVisibleState = false
    protected var mIsFirstVisible = true
    private var destroyed = false
    private var resumed = false
    private var sectionKey = ""
    internal val stateKey get() = sectionKey
    private lateinit var legacyLauncher: ActivityResultLauncher<Intent>
    private var resultIndex = 0
    private var visible = true
    private val registrations = mutableListOf<() -> Unit>()
    private val launchers = mutableListOf<ActivityResultLauncher<*>>()
    private var loadService: LoadService<*>? = null
    val context: Context? get() = mContext
    val activity: ScreenController? get() = mActivity
    val resources: Resources get() = requireContext().resources
    val isAdded get() = rootView != null && !destroyed
    val isHidden get() = !visible
    val userVisibleHint get() = visible
    fun requireContext(): Context = checkNotNull(mContext) { "Section is not attached" }
    fun requireActivity(): ScreenController = checkNotNull(mActivity) { "Section is not attached" }
    fun getRootView(): View? = rootView
    fun getView(): View? = rootView

    fun createView(context: Context, container: ViewGroup? = null): View {
        check(!destroyed) { "Cannot reuse a destroyed section" }
        mActivity = context as ScreenController
        mContext = context
        sectionKey = requireActivity().addSection(this)
        legacyLauncher = requireActivity().registerLegacyResult(sectionKey) { request, code, data ->
            if (!destroyed) onActivityResult(request, code, data)
        }
        rootView = LayoutInflater.from(context).inflate(getLayoutResID(), container, false)
        registry.currentState = Lifecycle.State.CREATED
        registrations.forEach { it() }
        val savedState = requireActivity().sectionState(sectionKey)
        onViewCreated(rootView!!, savedState)
        if (visible) activate()
        savedState?.getSparseParcelableArray<android.os.Parcelable>("views")?.let {
            rootView!!.restoreHierarchyState(it)
        }
        return rootView!!
    }

    open fun onViewCreated(view: View, savedInstanceState: Bundle?) {}
    open fun onSaveInstanceState(outState: Bundle) {
        val hierarchy = android.util.SparseArray<android.os.Parcelable>()
        rootView?.saveHierarchyState(hierarchy)
        outState.putSparseParcelableArray("views", hierarchy)
    }
    fun setUserVisibleHint(value: Boolean) { onHiddenChanged(!value) }
    open fun onHiddenChanged(hidden: Boolean) {
        visible = !hidden
        if (visible && rootView != null) activate() else pause()
    }
    private fun activate() {
        if (mIsFirstVisible) { mIsFirstVisible = false; init() }
        if (!currentVisibleState) { currentVisibleState = true; onFragmentResume() }
        if (requireActivity().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) resume()
    }
    fun dispatchUserVisibleHint(value: Boolean) = setUserVisibleHint(value)
    internal fun resume() {
        if (!visible || destroyed || rootView == null || resumed) return
        resumed = true
        if (mIsFirstVisible) { mIsFirstVisible = false; init() }
        if (!currentVisibleState) { currentVisibleState = true; onFragmentResume() }
        registry.currentState = Lifecycle.State.RESUMED
        onResume()
    }
    internal fun pause() {
        if (destroyed || rootView == null) return
        if (resumed) { resumed = false; onPause() }
        if (currentVisibleState) { currentVisibleState = false; onFragmentPause() }
        registry.currentState = Lifecycle.State.CREATED
    }
    open fun onResume() {}
    open fun onPause() {}
    protected open fun onFragmentResume() {}
    protected open fun onFragmentPause() {}
    open fun onDestroyView() { rootView = null; loadService = null }
    open fun onDestroy() {}
    fun destroy() {
        if (destroyed) return
        pause()
        destroyed = true
        onDestroyView()
        onDestroy()
        registry.currentState = Lifecycle.State.DESTROYED
        viewModelStore.clear()
        launchers.forEach { it.unregister() }
        launchers.clear()
        registrations.clear()
        mActivity?.removeSection(this)
        mActivity = null
        mContext = null
    }

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
    fun startActivity(intent: Intent) { requireActivity().startActivity(intent) }
    fun startActivityForResult(intent: Intent, requestCode: Int) {
        requireActivity().launchLegacyResult(sectionKey, legacyLauncher, intent, requestCode)
    }
    override fun <I, O> registerForActivityResult(contract: ActivityResultContract<I, O>, callback: ActivityResultCallback<O>): ActivityResultLauncher<I> =
        deferredLauncher(contract, null, callback)
    override fun <I, O> registerForActivityResult(contract: ActivityResultContract<I, O>, registry: ActivityResultRegistry, callback: ActivityResultCallback<O>): ActivityResultLauncher<I> =
        deferredLauncher(contract, registry, callback)

    private fun <I, O> deferredLauncher(contract: ActivityResultContract<I, O>, registry: ActivityResultRegistry?, callback: ActivityResultCallback<O>): ActivityResultLauncher<I> {
        val index = resultIndex++
        var real: ActivityResultLauncher<I>? = null
        val register = {
            real = (registry ?: requireActivity().hostActivity.activityResultRegistry).register(sectionKey + ":result:" + index, contract) { output ->
                mActivity?.dispatchResult { if (!destroyed) callback.onActivityResult(output) }
            }.also { launchers.add(it) }
        }
        registrations.add(register)
        return object : ActivityResultLauncher<I>() {
            override fun launch(input: I, options: ActivityOptionsCompat?) { checkNotNull(real) { "Section is not attached" }.launch(input, options) }
            override fun unregister() { real?.unregister() }
            override val contract: ActivityResultContract<I, *> = contract
        }
    }

    fun <T : View> findViewById(id: Int): T? = rootView?.findViewById(id)
    protected abstract fun getLayoutResID(): Int
    protected abstract fun init()
    protected fun setLoadSir(view: View) { if (loadService == null) loadService = LoadSir.getDefault().register(view) {} }
    protected fun setLoadSir2(view: View) { loadService = LoadSir.getDefault().register(view) {} }
    protected open fun showLoading() { loadService?.showCallback(LoadingCallback::class.java) }
    protected open fun showEmpty() { loadService?.showCallback(EmptyCallback::class.java) }
    protected open fun showSuccess() { loadService?.showSuccess() }
    fun jumpActivity(clazz: Class<out ScreenController>, bundle: Bundle? = null) { requireActivity().jumpActivity(clazz, bundle) }
    override fun getSizeInDp(): Float = requireActivity().sizeInDp
    override fun isBaseOnWidth(): Boolean = requireActivity().isBaseOnWidth
}
