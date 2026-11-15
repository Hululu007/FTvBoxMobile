package com.github.tvbox.osc.util

import android.app.Activity
import com.github.tvbox.osc.base.ScreenController
import com.github.tvbox.osc.ui.activity.MainActivity
import java.lang.ref.WeakReference

/** Tracks the single Android host; page navigation belongs to its Compose back stack. */
class AppManager private constructor() {
    private var host = WeakReference<Activity>(null)
    fun addActivity(activity: Activity) { host = WeakReference(activity) }
    fun isActivity(): Boolean = currentActivity() != null
    fun currentActivity(): Activity? = host.get()?.takeUnless { it.isDestroyed }
    fun currentScreen(): ScreenController? = (currentActivity() as? MainActivity)?.currentScreen
    fun finishActivity() { currentScreen()?.finish() ?: currentActivity()?.finish() }
    fun finishActivity(activity: Activity?) { if (host.get() === activity) host.clear() }
    fun finishActivity(cls: Class<*>) { getActivity(cls)?.takeIf { it === currentScreen() }?.finish() }
    fun backActivity(cls: Class<*>) { (currentActivity() as? MainActivity)?.backToScreen(cls) }
    fun finishAllActivity() { currentActivity()?.finish() }
    fun getActivity(cls: Class<*>): ScreenController? = (currentActivity() as? MainActivity)?.findScreen(cls)
    fun appExit(code: Int) { finishAllActivity() }
    companion object {
        private val instance = AppManager()
        @JvmStatic fun getInstance(): AppManager = instance
    }
}
