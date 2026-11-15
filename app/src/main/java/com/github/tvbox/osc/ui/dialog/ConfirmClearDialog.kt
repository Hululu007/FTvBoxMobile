package com.github.tvbox.osc.ui.dialog

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.cache.RoomDataManger
import com.github.tvbox.osc.cache.VodCollect
import com.github.tvbox.osc.ui.activity.CollectActivity
import com.github.tvbox.osc.ui.activity.HistoryActivity
import com.github.tvbox.osc.ui.compose.ConfirmDialogContent
import java.util.ArrayList

class ConfirmClearDialog(context: Context, type: String?) : BaseDialog(context) {

    init {
        sheetLayoutName = "dialog_confirm"
        setCanceledOnTouchOutside(true)
        val msg = if ("Collect" == type) "清空全部收藏？此操作无法撤销。" else "清空全部观看记录？此操作无法撤销。"

        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    ConfirmDialogContent(
                        message = msg,
                        onConfirm = {
                            if ("Collect" == type) {
                                val vodInfoList: List<VodCollect> = ArrayList()
                                CollectActivity.collectAdapter?.setNewData(vodInfoList)
                                CollectActivity.collectAdapter?.notifyDataSetChanged()
                                RoomDataManger.deleteVodCollectAll()
                            } else if ("History" == type) {
                                val vodInfoList: List<com.github.tvbox.osc.bean.VodInfo> = ArrayList()
                                HistoryActivity.historyAdapter?.setNewData(vodInfoList)
                                HistoryActivity.historyAdapter?.notifyDataSetChanged()
                                RoomDataManger.deleteVodRecordAll()
                            }
                            dismiss()
                        },
                        onCancel = {
                            dismiss()
                        }
                    )
                }
            }
        }
        setContentView(composeView)
    }
}
