package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.ui.compose.SettingMenuItem
import java.util.ArrayList

class SettingMenuAdapter : BaseQuickAdapter<String, BaseViewHolder>(0, ArrayList()) {
    var selectedPosition: Int = 0

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(helper: BaseViewHolder, item: String?) {
        val composeView = helper.itemView as ComposeView
        val position = helper.adapterPosition
        val isSelected = position == selectedPosition

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                SettingMenuItem(
                    name = item ?: "",
                    isSelected = isSelected,
                    onClick = {
                        val pos = helper.adapterPosition
                        if (pos in 0 until data.size) {
                            selectedPosition = pos
                            notifyDataSetChanged()
                            onItemClickListener?.onItemClick(this@SettingMenuAdapter, composeView, pos)
                            onItemChildClickListener?.onItemChildClick(this@SettingMenuAdapter, composeView, pos)
                        }
                    }
                )
            }
        }
    }
}
