package com.github.tvbox.osc.ui.dialog

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.ui.compose.XwalkDialogContent
import com.github.tvbox.osc.util.FastClickCheckUtil
import com.github.tvbox.osc.util.XWalkUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.FileCallback
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import java.io.File

class XWalkInitDialog(context: Context) : BaseDialog(context) {
    private var listener: OnListener? = null
    private var buttonText by mutableStateOf("下载")
    private var buttonEnabled by mutableStateOf(true)

    init {
        sheetLayoutName = "dialog_xwalk"
        setCanceledOnTouchOutside(false)
        setCancelable(true)
        setOnDismissListener {
            OkGo.getInstance().cancelTag("down_xwalk")
        }

        if (XWalkUtils.xWalkLibExist(context)) {
            buttonText = "重新下载"
        }

        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    XwalkDialogContent(
                        archInfo = "Arch:${XWalkUtils.getRuntimeAbi()}",
                        buttonText = buttonText,
                        enabled = buttonEnabled,
                        onDownloadClick = {
                            startDownload()
                        }
                    )
                }
            }
        }
        setContentView(composeView)
    }

    private fun startDownload() {
        buttonEnabled = false
        OkGo.get<File>(XWalkUtils.downUrl()).tag("down_xwalk").execute(object : FileCallback(context.cacheDir.absolutePath, XWalkUtils.saveZipFile()) {
            override fun onSuccess(response: Response<File>) {
                try {
                    XWalkUtils.unzipXWalkZip(context, response.body().absolutePath)
                    XWalkUtils.extractXWalkLib(context)
                    buttonText = "重新下载"
                    listener?.onchange()
                    dismiss()
                } catch (e: Throwable) {
                    e.printStackTrace()
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    buttonEnabled = true
                }
            }

            override fun onError(response: Response<File>) {
                super.onError(response)
                val errorMsg = response.exception?.message ?: "下载失败"
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                buttonEnabled = true
            }

            override fun downloadProgress(progress: Progress) {
                super.downloadProgress(progress)
                buttonText = String.format("%.2f%%", progress.fraction * 100)
            }
        })
    }

    fun setOnListener(listener: OnListener?): XWalkInitDialog {
        this.listener = listener
        return this
    }

    interface OnListener {
        fun onchange()
    }
}
