package com.github.tvbox.osc.ui.dialog

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.event.RefreshEvent
import com.github.tvbox.osc.ui.adapter.ApiHistoryDialogAdapter
import com.github.tvbox.osc.ui.compose.ApiDialogContent
import com.github.tvbox.osc.util.HawkConfig
import com.github.tvbox.osc.util.HistoryHelper
import com.orhanobut.hawk.Hawk
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

/**
 * 描述
 *
 * @author pj567
 * @since 2020/12/27
 */
class ApiDialog(context: Context) : BaseDialog(context) {
    private var apiText by mutableStateOf("")
    private var liveApiText by mutableStateOf("")
    private var listener: OnListener? = null

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refresh(event: RefreshEvent) {
        if (event.type == RefreshEvent.TYPE_API_URL_CHANGE) {
            val url = (event.obj as? String) ?: ""
            apiText = url
            liveApiText = url
        } else if (event.type == RefreshEvent.TYPE_LIVE_API_URL_CHANGE) {
            liveApiText = (event.obj as? String) ?: ""
        }
    }

    init {
        sheetLayoutName = "dialog_api"
        setCanceledOnTouchOutside(false)

        apiText = Hawk.get(HawkConfig.API_URL, "")
        liveApiText = Hawk.get(HawkConfig.LIVE_API_URL, Hawk.get(HawkConfig.API_URL, ""))

        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    ApiDialogContent(
                        apiText = apiText,
                        onApiTextChange = { apiText = it },
                        liveApiText = liveApiText,
                        onLiveApiTextChange = { liveApiText = it },
                        onPasteApi = { pasteInto { apiText = it } },
                        onSelectFileApi = { listener?.onLocalConfig(false) },
                        onPasteLive = { pasteInto { liveApiText = it } },
                        onSelectFileLive = { listener?.onLocalConfig(true) },
                        onLiveHistory = { openLiveHistory() },
                        onCancel = { cancel() },
                        onConfirm = { saveAndDismiss() }
                    )
                }
            }
        }
        setContentView(composeView)
    }

    private fun openLiveHistory() {
        val history = Hawk.get(HawkConfig.LIVE_API_HISTORY, ArrayList<String>())
        if (history.isEmpty()) {
            Toast.makeText(context, "直播历史为空", Toast.LENGTH_SHORT).show()
            return
        }
        val current = Hawk.get(HawkConfig.LIVE_API_URL, "")
        var idx = 0
        if (history.contains(current)) {
            idx = history.indexOf(current)
        }
        val dialog = ApiHistoryDialog(context)
        dialog.setTip("直播历史配置")
        dialog.setAdapter(object : ApiHistoryDialogAdapter.SelectDialogInterface {
            override fun click(value: String) {
                liveApiText = value
                dialog.dismiss()
            }

            override fun del(value: String, data: ArrayList<String>) {
                Hawk.put(HawkConfig.LIVE_API_HISTORY, data)
            }
        }, history, idx)
        dialog.show()
    }

    private fun pasteInto(setter: (String) -> Unit) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        if (clipboard == null || !clipboard.hasPrimaryClip() || clipboard.primaryClip == null) {
            Toast.makeText(context, "剪贴板中没有内容", Toast.LENGTH_SHORT).show()
            return
        }
        val value = clipboard.primaryClip?.getItemAt(0)?.coerceToText(context)
        setter(value?.toString()?.trim() ?: "")
    }

    private fun saveAndDismiss() {
        val newApi = apiText.trim()
        var newLiveApi = liveApiText.trim()
        if (newApi.isNotEmpty()) {
            HistoryHelper.setApiHistory(newApi)
        }
        if (newLiveApi.isEmpty()) newLiveApi = newApi
        if (newLiveApi.isNotEmpty()) {
            HistoryHelper.setLiveApiHistory(newLiveApi)
        }
        Hawk.put(HawkConfig.LIVE_API_URL, newLiveApi)
        listener?.onchange(newApi)
        dismiss()
    }

    fun setOnListener(listener: OnListener?) {
        this.listener = listener
    }

    fun setLocalApi(api: String?, live: Boolean) {
        if (live) {
            liveApiText = api ?: ""
        } else {
            apiText = api ?: ""
        }
    }

    interface OnListener {
        fun onchange(api: String)
        fun onLocalConfig(live: Boolean)
    }
}
