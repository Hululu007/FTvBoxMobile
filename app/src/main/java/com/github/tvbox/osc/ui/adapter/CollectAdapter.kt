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
import com.github.tvbox.osc.cache.VodCollect
import com.github.tvbox.osc.ui.compose.LibraryMobileItem
import com.github.tvbox.osc.util.HawkConfig
import java.util.ArrayList

class CollectAdapter : BaseQuickAdapter<VodCollect, BaseViewHolder>(0, ArrayList()) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(helper: BaseViewHolder, item: VodCollect?) {
        if (item == null) return
        val source = ApiConfig.get().getSource(item.sourceKey)
        val sourceName = source?.name ?: ""
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
                    sourceOrYear = sourceName,
                    note = null,
                    pic = item.pic,
                    isDeleteMode = isDeleteMode,
                    onClick = {
                        val position = helper.adapterPosition
                        if (position in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@CollectAdapter, composeView, position)
                        }
                    },
                    onDeleteClick = {
                        val position = helper.adapterPosition
                        if (position in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@CollectAdapter, composeView, position)
                        }
                    }
                )
            }
        }
    }
}
