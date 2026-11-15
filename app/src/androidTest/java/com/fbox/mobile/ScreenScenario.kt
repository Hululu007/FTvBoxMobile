package com.fbox.mobile

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.github.tvbox.osc.base.ScreenController
import com.github.tvbox.osc.ui.activity.MainActivity
import com.github.tvbox.osc.ui.activity.ScreenIntents
import java.io.Closeable

fun <T : android.view.View> ScreenController.requireViewById(id: Int): T = checkNotNull(findViewById(id))

/** Runs the existing behavioral fixtures through the real single-Activity navigation host. */
class ScreenScenario<T : ScreenController> private constructor(
    private val scenario: ActivityScenario<MainActivity>, private val type: Class<T>
) : Closeable {
    val state get() = scenario.state
    fun onActivity(action: (T) -> Unit): ScreenScenario<T> {
        scenario.onActivity { host -> action(checkNotNull(type.cast(checkNotNull(host.findScreen(type))))) }
        return this
    }
    fun recreate(): ScreenScenario<T> { scenario.recreate(); awaitScreen(); return this }
    fun moveToState(state: Lifecycle.State): ScreenScenario<T> { scenario.moveToState(state); return this }
    private fun awaitScreen() {
        val deadline = android.os.SystemClock.uptimeMillis() + 10000
        while (true) {
            var ready = false
            scenario.onActivity {
                ready = type.isInstance(it.currentScreen) &&
                    it.currentScreen?.lifecycle?.currentState == Lifecycle.State.RESUMED
            }
            if (ready) return
            check(android.os.SystemClock.uptimeMillis() < deadline) { "Screen was not created: " + type.name }
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
            android.os.SystemClock.sleep(50)
        }
    }
    override fun close() { scenario.close() }
    companion object {
        fun <T : ScreenController> launch(type: Class<T>): ScreenScenario<T> {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            return launch(ScreenIntents.create(context, type))
        }
        @Suppress("UNCHECKED_CAST")
        fun <T : ScreenController> launch(intent: Intent): ScreenScenario<T> {
            val type = Class.forName(intent.getStringExtra(ScreenIntents.EXTRA_SCREEN) ?: intent.component!!.className) as Class<T>
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val target = ScreenIntents.create(context, type).putExtras(intent.extras ?: android.os.Bundle())
            val instance = ScreenScenario(ActivityScenario.launch<MainActivity>(target), type)
            instance.awaitScreen()
            return instance
        }
    }
}
