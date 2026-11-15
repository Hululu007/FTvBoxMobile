package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.ui.compose.GridFilterValueItem
import java.util.ArrayList

class GridFilterKVAdapter : BaseQuickAdapter<String, BaseViewHolder>(0, ArrayList()) {
    private var selectedPosition = -1

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(helper: BaseViewHolder, item: String?) {
        val text = item ?: ""
        val selected = helper.layoutPosition == selectedPosition
        val composeView = helper.itemView as ComposeView

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                GridFilterValueItem(
                    text = text,
                    isSelected = selected,
                    onClick = {
                        val position = helper.adapterPosition
                        if (position in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@GridFilterKVAdapter, composeView, position)
                        }
                    }
                )
            }
        }
    }
}
