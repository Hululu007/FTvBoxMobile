package com.github.tvbox.osc.ui.dialog

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.ui.compose.AboutDialogContent

class AboutDialog(context: Context) : BaseDialog(context) {
    init {
        sheetLayoutName = "dialog_about"
        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    AboutDialogContent()
                }
            }
        }
        setContentView(composeView)
    }
}
