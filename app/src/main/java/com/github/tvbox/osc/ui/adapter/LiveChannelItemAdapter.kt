package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.bean.LiveChannelItem
import com.github.tvbox.osc.ui.compose.LiveChannelItem
import java.util.ArrayList

class LiveChannelItemAdapter : BaseQuickAdapter<LiveChannelItem, BaseViewHolder>(0, ArrayList()) {
    private var selectedChannelIndex = -1
    private var focusedChannelIndex = -1

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(holder: BaseViewHolder, item: LiveChannelItem?) {
        if (item == null) return
        val composeView = holder.itemView as ComposeView
        val channelIndex = item.getChannelIndex()
        val isSelected = channelIndex == selectedChannelIndex
        val isFocused = channelIndex == focusedChannelIndex

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                LiveChannelItem(
                    channelNum = item.getChannelNum().toString(),
                    channelName = item.getChannelName() ?: "",
                    isSelected = isSelected,
                    isFocused = isFocused,
                    onClick = {
                        val pos = holder.adapterPosition
                        if (pos in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@LiveChannelItemAdapter, composeView, pos)
                        }
                    }
                )
            }
        }
    }

    fun setSelectedChannelIndex(selectedChannelIndex: Int) {
        if (selectedChannelIndex == this.selectedChannelIndex) return
        val preSelectedChannelIndex = this.selectedChannelIndex
        this.selectedChannelIndex = selectedChannelIndex
        if (preSelectedChannelIndex != -1) {
            notifyItemChanged(preSelectedChannelIndex)
        }
        if (this.selectedChannelIndex != -1) {
            notifyItemChanged(this.selectedChannelIndex)
        }
    }

    fun setFocusedChannelIndex(focusedChannelIndex: Int) {
        val preFocusedChannelIndex = this.focusedChannelIndex
        this.focusedChannelIndex = focusedChannelIndex
        if (preFocusedChannelIndex != -1) {
            notifyItemChanged(preFocusedChannelIndex)
        }
        if (this.focusedChannelIndex != -1) {
            notifyItemChanged(this.focusedChannelIndex)
        } else if (this.selectedChannelIndex != -1) {
            notifyItemChanged(this.selectedChannelIndex)
        }
    }
}
