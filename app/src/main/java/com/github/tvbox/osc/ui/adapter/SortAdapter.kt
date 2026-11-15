package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.bean.MovieSort
import com.github.tvbox.osc.ui.compose.HomeSortItem
import java.util.ArrayList

class SortAdapter : BaseQuickAdapter<MovieSort.SortData, BaseViewHolder>(0, ArrayList()) {
    private var selectedPosition = 0

    fun setSelectedPosition(position: Int) {
        val previous = selectedPosition
        selectedPosition = position
        notifyItemChanged(previous)
        notifyItemChanged(position)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(helper: BaseViewHolder, item: MovieSort.SortData?) {
        if (item == null) return
        val title = item.name ?: ""
        val selected = helper.adapterPosition == selectedPosition
        val hasFilters = item.filters != null && item.filters.isNotEmpty()
        val composeView = helper.itemView as ComposeView

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                HomeSortItem(
                    title = title,
                    isSelected = selected,
                    hasFilters = hasFilters,
                    onClick = {
                        val position = helper.adapterPosition
                        if (position in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@SortAdapter, composeView, position)
                        }
                    }
                )
            }
        }
    }
}
