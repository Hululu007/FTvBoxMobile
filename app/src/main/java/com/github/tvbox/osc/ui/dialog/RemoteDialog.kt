package com.github.tvbox.osc.ui.dialog

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.server.ControlManager
import com.github.tvbox.osc.ui.compose.RemoteDialogContent
import com.github.tvbox.osc.ui.tv.QRCodeGen
import me.jessyan.autosize.utils.AutoSizeUtils

class RemoteDialog(context: Context) : BaseDialog(context) {
    private var addressText by mutableStateOf("")
    private var qrBitmapState by mutableStateOf<Bitmap?>(null)

    init {
        sheetLayoutName = "dialog_remote"
        setCanceledOnTouchOutside(false)
        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    RemoteDialogContent(
                        address = addressText,
                        qrBitmap = qrBitmapState
                    )
                }
            }
        }
        setContentView(composeView)
        refreshQRCode()
    }

    private fun refreshQRCode() {
        val address = ControlManager.get().getAddress(false)
        addressText = address ?: ""
        qrBitmapState = QRCodeGen.generateBitmap(
            address,
            AutoSizeUtils.mm2px(context, 240f),
            AutoSizeUtils.mm2px(context, 240f)
        )
    }
}
