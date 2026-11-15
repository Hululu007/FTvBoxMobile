package com.github.tvbox.osc.ui.dialog

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.ui.compose.TipDialogContent

class TipDialog(
    context: Context,
    tip: String?,
    left: String?,
    right: String?,
    listener: OnListener
) : BaseDialog(context) {

    init {
        sheetLayoutName = "dialog_tip"
        setCanceledOnTouchOutside(false)
        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    TipDialogContent(
                        tip = tip ?: "",
                        leftText = left ?: "确定",
                        rightText = right ?: "取消",
                        onLeftClick = { listener.left() },
                        onRightClick = { listener.right() }
                    )
                }
            }
        }
        setContentView(composeView)
        setOnCancelListener {
            listener.cancel()
        }
    }

    interface OnListener {
        fun left()
        fun right()
        fun cancel()
    }
}
