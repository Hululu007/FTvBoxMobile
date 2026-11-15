package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.bean.Subtitle
import com.github.tvbox.osc.ui.compose.SearchSubtitleResultItem
import java.util.ArrayList

class SearchSubtitleAdapter : BaseQuickAdapter<Subtitle, BaseViewHolder>(0, ArrayList()) {
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(helper: BaseViewHolder, item: Subtitle?) {
        if (item == null) return
        val composeView = helper.itemView as ComposeView

        val name = item.getName() ?: ""
        val info = if (item.getIsZip()) "压缩包" else "文件"

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                SearchSubtitleResultItem(
                    name = name,
                    info = info,
                    isSelected = false,
                    onClick = {
                        val pos = helper.adapterPosition
                        if (pos in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@SearchSubtitleAdapter, composeView, pos)
                        }
                    }
                )
            }
        }
    }
}
