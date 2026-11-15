package com.github.tvbox.osc.base

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.findHostActivity(): Activity? {
    var current: Context = this
    while (true) {
        if (current is Activity) return current
        val next = (current as? ContextWrapper)?.baseContext ?: return null
        if (next === current) return null
        current = next
    }
}

fun Context.findScreenController(): ScreenController? {
    var current: Context = this
    while (true) {
        if (current is ScreenController) return current
        val next = (current as? ContextWrapper)?.baseContext ?: return null
        if (next === current) return null
        current = next
    }
}
