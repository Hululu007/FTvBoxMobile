package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.api.ApiConfig
import com.github.tvbox.osc.bean.VodInfo
import com.github.tvbox.osc.ui.compose.LibraryMobileItem
import com.github.tvbox.osc.util.HawkConfig
import java.util.ArrayList

class HistoryAdapter : BaseQuickAdapter<VodInfo, BaseViewHolder>(0, ArrayList()) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(helper: BaseViewHolder, item: VodInfo?) {
        if (item == null) return
        val bean = ApiConfig.get().getSource(item.sourceKey)
        val sourceOrYear = bean?.name ?: "搜"
        val displayName = item.name ?: ""
        val isDeleteMode = HawkConfig.hotVodDelete
        val composeView = helper.itemView as ComposeView

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                LibraryMobileItem(
                    name = displayName,
                    sourceOrYear = sourceOrYear,
                    note = item.note,
                    pic = item.pic,
                    isDeleteMode = isDeleteMode,
                    onClick = {
                        val position = helper.adapterPosition
                        if (position in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@HistoryAdapter, composeView, position)
                        }
                    },
                    onDeleteClick = {
                        val position = helper.adapterPosition
                        if (position in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@HistoryAdapter, composeView, position)
                        }
                    }
                )
            }
        }
    }
}
