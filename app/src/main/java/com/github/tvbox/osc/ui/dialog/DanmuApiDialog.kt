package com.github.tvbox.osc.ui.dialog

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.api.DanmakuApi
import com.github.tvbox.osc.ui.compose.DanmuApiDialogContent
import com.github.tvbox.osc.util.HawkConfig
import com.orhanobut.hawk.Hawk

class DanmuApiDialog(context: Context) : BaseDialog(context) {
    private var listener: OnListener? = null

    init {
        sheetLayoutName = "dialog_danmu_api"
        setCanceledOnTouchOutside(false)
        val initialText = Hawk.get(HawkConfig.DANMU_API, "")
        val defaultHint = getDefaultApi()

        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    DanmuApiDialogContent(
                        initialValue = initialText,
                        hintText = defaultHint,
                        onDefaultClick = { saveDefault() },
                        onSubmitClick = { api -> save(api) }
                    )
                }
            }
        }
        setContentView(composeView)
    }

    private fun getDefaultApi(): String {
        val api = DanmakuApi.getDisplayApiUrl()
        return if (api.isEmpty()) "请输入弹幕搜索地址" else api
    }

    private fun save(api: String) {
        DanmakuApi.setCustomApi(api)
        listener?.onChange(api)
        dismiss()
    }

    private fun saveDefault() {
        DanmakuApi.setUseDefault(true)
        listener?.onChange("")
        dismiss()
    }

    fun setOnListener(listener: OnListener?) {
        this.listener = listener
    }

    fun interface OnListener {
        fun onChange(api: String)
    }
}
