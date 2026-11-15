package com.github.tvbox.osc.callback

import android.content.Context
import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.ui.compose.EmptyStateView
import com.kingja.loadsir.callback.Callback

class EmptyCallback : Callback() {
    override fun onCreateView(): Int {
        return 0
    }

    override fun onBuildView(context: Context): View {
        return ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    EmptyStateView()
                }
            }
        }
    }
}
