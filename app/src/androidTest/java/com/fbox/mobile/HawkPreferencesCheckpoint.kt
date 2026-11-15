package com.fbox.mobile

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.github.tvbox.osc.util.HawkConfig
import com.orhanobut.hawk.Hawk
import org.json.JSONObject
import org.junit.rules.ExternalResource

/** Restores encrypted preferences even when a previous instrumentation process crashed. */
class HawkPreferencesCheckpoint : ExternalResource() {
    private val instrumentation get() = InstrumentationRegistry.getInstrumentation()
    private val target get() = instrumentation.targetContext.getSharedPreferences("Hawk2", Context.MODE_PRIVATE)
    private val checkpoint get() = instrumentation.targetContext.getSharedPreferences("review_fixture_checkpoint", Context.MODE_PRIVATE)

    override fun before() {
        restore()
        // Remove the known abandoned fixture from runs made before checkpoint support.
        if (Hawk.get(HawkConfig.API_URL, "") == "https://compose-checkbox.invalid/config") {
            Hawk.delete(HawkConfig.API_URL)
        }
        check(checkpoint.edit().putString("Hawk2", JSONObject(target.all).toString()).commit())
    }

    override fun after() {
        restore()
    }

    private fun restore() {
        val saved = checkpoint.getString("Hawk2", null) ?: return
        val json = JSONObject(saved)
        val editor = target.edit().clear()
        // Hawk's SharedPreferencesStorage stores serialized, encrypted String values.
        for (key in json.keys()) editor.putString(key, json.getString(key))
        check(editor.commit())
        check(checkpoint.edit().remove("Hawk2").commit())
    }
}
