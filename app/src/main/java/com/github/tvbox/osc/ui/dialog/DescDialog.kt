package com.github.tvbox.osc.ui.dialog

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.ui.compose.DescDialogContent

class DescDialog(context: Context) : BaseDialog(context) {

    private var describeText by mutableStateOf("")

    init {
        sheetLayoutName = "dialog_desc"
        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    DescDialogContent(describe = describeText)
                }
            }
        }
        setContentView(composeView)
    }

    fun setDescribe(describe: String?) {
        describeText = describe ?: ""
    }
}
