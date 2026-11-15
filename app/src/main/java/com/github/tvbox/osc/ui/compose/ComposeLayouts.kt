package com.github.tvbox.osc.ui.compose

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.tvbox.osc.R

/**
 * Compatibility layouts for screens whose controllers still bind Views synchronously.
 *
 * Activity.init() and Fragment.onViewCreated() run before a Compose AndroidView factory.
 * Keep the real hierarchy attached at that point: detached or hidden placeholder Views
 * cannot forward visibility, text, layout changes, or FragmentManager container lookup.
 * These screens can move to Compose once their state and actions have been hoisted.
 */
object ComposeLayouts {
    fun createActivityLayout(context: Context, layoutResId: Int): View? = when (layoutResId) {
        R.layout.activity_collect, R.layout.activity_history, R.layout.activity_push,
        R.layout.activity_local_file, R.layout.activity_setting, R.layout.activity_home,
        R.layout.activity_fast_search, R.layout.activity_search, R.layout.activity_detail,
        R.layout.activity_live_play -> inflate(context, layoutResId)
        else -> null
    }

    fun createFragmentLayout(context: Context, layoutResId: Int): View? = when (layoutResId) {
        R.layout.fragment_grid, R.layout.fragment_user, R.layout.fragment_model,
        R.layout.activity_play -> inflate(context, layoutResId)
        else -> null
    }

    private fun inflate(context: Context, layoutResId: Int): View =
        LayoutInflater.from(context).inflate(layoutResId, null, false)
}
