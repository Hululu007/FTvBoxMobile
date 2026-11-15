package com.github.tvbox.osc.base

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Looper
import android.view.ContextThemeWrapper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.github.tvbox.osc.R
import com.github.tvbox.osc.api.ApiConfig
import com.github.tvbox.osc.callback.EmptyCallback
import com.github.tvbox.osc.callback.LoadingCallback
import com.github.tvbox.osc.ui.activity.MainActivity
import com.github.tvbox.osc.ui.activity.LiveScreen
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import me.jessyan.autosize.AutoSizeCompat
import me.jessyan.autosize.internal.CustomAdapt
import xyz.doikki.videoplayer.util.CutoutUtil
import java.io.File

/**
 * Business controller for one Compose navigation entry. This is a Context wrapper,
 * never an Android Activity. MainActivity owns its lifetime and window.
 * Existing player and recycler Views stay live while their state is migrated.
 */
abstract class ScreenController : ContextThemeWrapper(), LifecycleOwner, ViewModelStoreOwner,
    ActivityResultCaller, CustomAdapt {
    lateinit var hostActivity: MainActivity
        private set
    lateinit var entryId: String
        private set
    lateinit var intent: Intent
        private set
    lateinit var contentView: View
        private set
    protected var mContext: Context? = null
    private val registry = LifecycleRegistry(this)
    override val lifecycle: Lifecycle get() = registry
    override val viewModelStore = ViewModelStore()
    private val sections = mutableListOf<ScreenSection>()
    private val launchers = mutableListOf<ActivityResultLauncher<*>>()
    private var nextResultKey = 0
    private var nextSectionKey = 0
    private var pendingRequests = Bundle()
    private var sectionStates = Bundle()
    private val pendingResults = mutableListOf<() -> Unit>()
    private lateinit var legacyLauncher: ActivityResultLauncher<Intent>
    private var loadService: LoadService<*>? = null
    var fullscreen by mutableStateOf(false)
        private set
    private var closing = false
    val isFinishing get() = closing || hostActivity.isFinishing
    var isDestroyed = false
        private set
    val isChangingConfigurations get() = hostActivity.isChangingConfigurations
    val window get() = hostActivity.window
    val windowManager get() = hostActivity.windowManager
    val application get() = hostActivity.application
    val currentFocus get() = contentView.findFocus()
    var requestedOrientation: Int = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        set(value) {
            field = value
            if (hostActivity.currentScreen === this) hostActivity.requestedOrientation = value
        }

    fun attach(host: MainActivity, id: String, arguments: Bundle, savedState: Bundle?) {
        hostActivity = host
        entryId = id
        attachBaseContext(host)
        mContext = this
        intent = Intent().putExtras(arguments)
        pendingRequests = savedState?.getBundle("pending_requests") ?: Bundle()
        sectionStates = savedState?.getBundle("sections") ?: Bundle()
        legacyLauncher = registerLegacyResult("screen") { request, code, data -> onActivityResult(request, code, data) }
        fullscreen = this is LiveScreen
        if (fullscreen) requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        onCreate(savedState)
        registry.currentState = Lifecycle.State.CREATED
    }

    open fun onCreate(savedInstanceState: Bundle?) {
        val layout = getLayoutResID()
        // LoadSir and synchronous view controllers need a real parent before init().
        contentView = FrameLayout(this).apply {
            if (layout != 0) addView(
                LayoutInflater.from(this@ScreenController).inflate(layout, this, false),
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            )
        }
        init()
        com.fbox.mobile.MobileActions.installScreen(contentView)
        contentView.findViewById<View>(R.id.mobileSearchBack)?.setOnClickListener { onBackPressed() }
    }

    open fun onStart() {
        registry.currentState = Lifecycle.State.STARTED
        val results = pendingResults.toList()
        pendingResults.clear()
        results.forEach { if (!isDestroyed) it() }
    }
    open fun onResume() {
        registry.currentState = Lifecycle.State.RESUMED
        requestedOrientation = requestedOrientation
        hideSysBar()
        sections.toList().forEach { it.resume() }
    }
    open fun onPause() {
        sections.toList().forEach { it.pause() }
        registry.currentState = Lifecycle.State.STARTED
    }
    open fun onStop() { registry.currentState = Lifecycle.State.CREATED }
    open fun onDestroy() {}
    open fun onSaveInstanceState(outState: Bundle) {
        val hierarchy = android.util.SparseArray<android.os.Parcelable>()
        contentView.saveHierarchyState(hierarchy)
        outState.putSparseParcelableArray("views", hierarchy)
        outState.putBundle("pending_requests", Bundle(pendingRequests))
        outState.putBundle("sections", Bundle(sectionStates).apply {
            sections.forEach { section -> putBundle(section.stateKey, Bundle().also(section::onSaveInstanceState)) }
        })
    }
    open fun onConfigurationChanged(newConfig: Configuration) { contentView.requestLayout() }
    open fun onWindowFocusChanged(hasFocus: Boolean) { if (hasFocus) hideSysBar() }

    fun destroy() {
        if (isDestroyed) return
        if (lifecycle.currentState == Lifecycle.State.RESUMED) onPause()
        sections.toList().forEach { it.destroy() }
        isDestroyed = true
        onDestroy()
        registry.currentState = Lifecycle.State.DESTROYED
        launchers.forEach { it.unregister() }
        launchers.clear()
        pendingResults.clear()
        viewModelStore.clear()
        (contentView.parent as? ViewGroup)?.removeView(contentView)
    }

    internal fun addSection(section: ScreenSection): String {
        sections.add(section)
        return entryId + ":section:" + nextSectionKey++
    }
    internal fun removeSection(section: ScreenSection) { sections.remove(section) }
    internal fun sectionState(key: String): Bundle? = sectionStates.getBundle(key)
    internal fun dispatchResult(action: () -> Unit) {
        if (isDestroyed) return
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) action()
        else pendingResults.add(action)
    }

    fun <T : View> findViewById(id: Int): T? = contentView.findViewById(id)
    fun setContentView(view: View) { contentView = view }
    fun addContentView(view: View, params: ViewGroup.LayoutParams) {
        (contentView as? ViewGroup)?.addView(view, params)
    }
    fun runOnUiThread(action: Runnable) = hostActivity.runOnUiThread(action)
    fun hasWindowFocus() = hostActivity.hasWindowFocus()
    open fun onBackPressed() { finish() }
    open fun dispatchKeyEvent(event: KeyEvent): Boolean = when (event.action) {
        KeyEvent.ACTION_DOWN -> onKeyDown(event.keyCode, event)
        KeyEvent.ACTION_UP -> onKeyUp(event.keyCode, event)
        else -> false
    }
    open fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean = false
    open fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean = false
    fun finish() { closing = true; hostActivity.closeScreen(this) }
    fun setResult(code: Int, data: Intent?) { hostActivity.deliverScreenResult(this, code, data) }
    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
    fun startActivityForResult(intent: Intent, requestCode: Int) {
        launchLegacyResult("screen", legacyLauncher, intent, requestCode)
    }

    internal fun registerLegacyResult(key: String, callback: (Int, Int, Intent?) -> Unit): ActivityResultLauncher<Intent> {
        val launcher = hostActivity.activityResultRegistry.register(entryId + ":legacy:" + key, ActivityResultContracts.StartActivityForResult()) { result ->
            dispatchResult { callback(pendingRequests.getInt(key), result.resultCode, result.data) }
        }
        launchers.add(launcher)
        return launcher
    }

    internal fun launchLegacyResult(key: String, launcher: ActivityResultLauncher<Intent>, intent: Intent, requestCode: Int) {
        pendingRequests.putInt(key, requestCode)
        launcher.launch(intent)
    }

    override fun <I, O> registerForActivityResult(contract: ActivityResultContract<I, O>, callback: ActivityResultCallback<O>): ActivityResultLauncher<I> =
        registerForActivityResult(contract, hostActivity.activityResultRegistry, callback)

    override fun <I, O> registerForActivityResult(contract: ActivityResultContract<I, O>, registry: ActivityResultRegistry, callback: ActivityResultCallback<O>): ActivityResultLauncher<I> {
        // The registry-only overload supports entries created after Activity.onStart.
        // Keys are stable for this back stack entry and all launchers are explicitly cleared.
        val launcher = registry.register(entryId + ":result:" + nextResultKey++, contract) { value ->
            dispatchResult { callback.onActivityResult(value) }
        }
        launchers.add(launcher)
        return launcher
    }

    override fun startActivity(intent: Intent) {
        if (!hostActivity.openScreenIntent(intent)) super.startActivity(intent)
    }
    override fun startActivity(intent: Intent, options: Bundle?) {
        if (!hostActivity.openScreenIntent(intent)) super.startActivity(intent, options)
    }
    fun jumpActivity(clazz: Class<out ScreenController>, bundle: Bundle? = null) {
        startActivity(Intent(this, clazz).apply { if (bundle != null) putExtras(bundle) })
    }

    fun isPhonePortrait() = super.getResources().configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    fun setPhoneFullscreen(value: Boolean) { fullscreen = value; hideSysBar() }
    open fun hideSysBar() {
        if (hostActivity.currentScreen !== this) return
        CutoutUtil.adaptCutoutAboveAndroidP(hostActivity, fullscreen)
        if (fullscreen) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            window.statusBarColor = 0xFF10131A.toInt()
            window.navigationBarColor = 0xFF10131A.toInt()
        }
    }
    protected open fun shouldRefreshAutoSize() = false
    override fun getResources(): Resources {
        val resources = super.getResources()
        if (Looper.myLooper() == Looper.getMainLooper()) AutoSizeCompat.autoConvertDensityOfCustomAdapt(resources, this)
        return resources
    }
    fun hasPermission(permission: String) = PermissionChecker.checkSelfPermission(this, permission) == PermissionChecker.PERMISSION_GRANTED
    protected abstract fun getLayoutResID(): Int
    protected abstract fun init()
    protected fun withVodConfig(onReady: () -> Unit) {
        ApiConfig.get().ensureConfigLoaded(object : ApiConfig.LoadConfigCallback {
            override fun success() { if (!isFinishing && !isDestroyed) onReady() }
            override fun error(msg: String) {
                if (isFinishing || isDestroyed) return
                showEmpty()
                Toast.makeText(this@ScreenController, "配置加载失败：" + msg, Toast.LENGTH_LONG).show()
            }
            override fun notice(msg: String) {}
        })
    }
    protected fun setLoadSir(view: View) { if (loadService == null) loadService = LoadSir.getDefault().register(view) {} }
    protected open fun showLoading() { loadService?.showCallback(LoadingCallback::class.java) }
    protected open fun isLoading() = loadService?.currentCallback == LoadingCallback::class.java
    protected open fun showEmpty() { loadService?.showCallback(EmptyCallback::class.java) }
    protected open fun showSuccess() { loadService?.showSuccess() }
    protected fun getAssetText(fileName: String): String = try { assets.open(fileName).bufferedReader().use { it.readText() } } catch (_: Exception) { "" }
    override fun getSizeInDp(): Float = if (isPhonePortrait()) 392f else if (isBaseOnWidth) 1280f else 720f
    override fun isBaseOnWidth(): Boolean = isPhonePortrait() || super.getResources().displayMetrics.let { maxOf(it.widthPixels, it.heightPixels).toFloat() / minOf(it.widthPixels, it.heightPixels).coerceAtLeast(1) < 4f }
    open fun changeWallpaper(force: Boolean) {
        if (force || globalWp == null) {
            val file = File(filesDir, "wp")
            globalWp = if (file.exists()) {
                val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeFile(file.path, options)
                options.inSampleSize = maxOf(1, options.outWidth / 1080, options.outHeight / 720)
                options.inJustDecodeBounds = false
                BitmapFactory.decodeFile(file.path, options)?.let { BitmapDrawable(resources, it) }
            } else null
        }
        if (globalWp != null) window.setBackgroundDrawable(globalWp) else window.setBackgroundDrawableResource(R.drawable.app_bg)
    }
    companion object { private var globalWp: BitmapDrawable? = null }
}
