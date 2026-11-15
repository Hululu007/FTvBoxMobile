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
import com.github.tvbox.osc.bean.Movie
import com.github.tvbox.osc.ui.compose.SearchLiteItem
import com.github.tvbox.osc.ui.compose.SearchNormalItem
import com.github.tvbox.osc.util.HawkConfig
import com.orhanobut.hawk.Hawk
import java.util.ArrayList

open class SearchAdapter : BaseQuickAdapter<Movie.Video, BaseViewHolder>(
    0,
    ArrayList()
) {
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(helper: BaseViewHolder, item: Movie.Video?) {
        if (item == null) return
        val source = ApiConfig.get().getSource(item.sourceKey)
        val sourceName = source?.name ?: ""
        val composeView = helper.itemView as ComposeView

        val isLite = Hawk.get(HawkConfig.SEARCH_VIEW, 0) == 0

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                if (isLite) {
                    val text = String.format("%s\n%s · %s %s", item.name ?: "", sourceName, item.type ?: "", item.note ?: "")
                    SearchLiteItem(
                        name = text,
                        onClick = {
                            val pos = helper.adapterPosition
                            if (pos in 0 until data.size) {
                                onItemClickListener?.onItemClick(this@SearchAdapter, composeView, pos)
                            }
                        }
                    )
                } else {
                    SearchNormalItem(
                        name = item.name ?: "",
                        siteName = sourceName,
                        note = item.note ?: "",
                        thumbUrl = item.pic?.trim(),
                        onClick = {
                            val pos = helper.adapterPosition
                            if (pos in 0 until data.size) {
                                onItemClickListener?.onItemClick(this@SearchAdapter, composeView, pos)
                            }
                        }
                    )
                }
            }
        }
    }
}
