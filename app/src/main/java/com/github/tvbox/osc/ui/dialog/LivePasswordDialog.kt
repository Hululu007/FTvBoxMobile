package com.github.tvbox.osc.ui.dialog

import android.app.Activity
import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.ui.compose.LivePasswordDialogContent

/**
 * 描述
 *
 * @author pj567
 * @since 2020/12/27
 */
class LivePasswordDialog(context: Context) : BaseDialog(context) {
    private var listener: OnListener? = null

    init {
        sheetLayoutName = "dialog_live_password"
        if (context is Activity) {
            setOwnerActivity(context)
        }
        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    LivePasswordDialogContent(
                        onSubmit = { password ->
                            if (password.isNotEmpty()) {
                                listener?.onChange(password)
                                dismiss()
                            }
                        }
                    )
                }
            }
        }
        setContentView(composeView)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        listener?.onCancel()
        dismiss()
    }

    fun setOnListener(listener: OnListener?) {
        this.listener = listener
    }

    interface OnListener {
        fun onChange(password: String)
        fun onCancel()
    }
}
