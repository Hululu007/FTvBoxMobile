package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.bean.LiveChannelGroup
import com.github.tvbox.osc.ui.compose.LiveChannelGroupItem
import java.util.ArrayList

class LiveChannelGroupAdapter : BaseQuickAdapter<LiveChannelGroup, BaseViewHolder>(0, ArrayList()) {
    private var selectedGroupIndex = -1
    private var focusedGroupIndex = -1

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(holder: BaseViewHolder, item: LiveChannelGroup?) {
        if (item == null) return
        val composeView = holder.itemView as ComposeView
        val groupIndex = item.getGroupIndex()
        val isSelected = groupIndex == selectedGroupIndex
        val isFocused = groupIndex == focusedGroupIndex

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                LiveChannelGroupItem(
                    groupName = item.getGroupName() ?: "",
                    isSelected = isSelected,
                    isFocused = isFocused,
                    isEpgDateMode = false,
                    onClick = {
                        val pos = holder.adapterPosition
                        if (pos in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@LiveChannelGroupAdapter, composeView, pos)
                        }
                    }
                )
            }
        }
    }

    fun setSelectedGroupIndex(selectedGroupIndex: Int) {
        if (selectedGroupIndex == this.selectedGroupIndex) return
        val preSelectedGroupIndex = this.selectedGroupIndex
        this.selectedGroupIndex = selectedGroupIndex
        notifyGroupChanged(preSelectedGroupIndex)
        notifyGroupChanged(this.selectedGroupIndex)
    }

    fun getSelectedGroupIndex(): Int {
        return selectedGroupIndex
    }

    fun setFocusedGroupIndex(focusedGroupIndex: Int) {
        this.focusedGroupIndex = focusedGroupIndex
        if (this.focusedGroupIndex != -1) {
            notifyGroupChanged(this.focusedGroupIndex)
        } else if (this.selectedGroupIndex != -1) {
            notifyGroupChanged(this.selectedGroupIndex)
        }
    }

    fun clearGroupState() {
        selectedGroupIndex = -1
        focusedGroupIndex = -1
    }

    private fun notifyGroupChanged(position: Int) {
        if (position in 0 until itemCount) {
            notifyItemChanged(position)
        }
    }
}
