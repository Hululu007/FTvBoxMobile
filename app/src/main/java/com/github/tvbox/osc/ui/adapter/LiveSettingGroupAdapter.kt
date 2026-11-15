package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.bean.LiveSettingGroup
import com.github.tvbox.osc.ui.compose.LiveSettingGroupItem
import java.util.ArrayList

class LiveSettingGroupAdapter : BaseQuickAdapter<LiveSettingGroup, BaseViewHolder>(0, ArrayList()) {
    private var selectedGroupIndex = -1
    private var focusedGroupIndex = -1

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(holder: BaseViewHolder, group: LiveSettingGroup?) {
        if (group == null) return
        val composeView = holder.itemView as ComposeView
        val groupIndex = group.getGroupIndex()
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
                LiveSettingGroupItem(
                    groupName = group.getGroupName() ?: "",
                    isSelected = isSelected,
                    isFocused = isFocused,
                    onClick = {
                        val pos = holder.adapterPosition
                        if (pos in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@LiveSettingGroupAdapter, composeView, pos)
                        }
                    }
                )
            }
        }
    }

    fun setSelectedGroupIndex(selectedGroupIndex: Int) {
        val preSelectedGroupIndex = this.selectedGroupIndex
        this.selectedGroupIndex = if (findPositionByGroupIndex(selectedGroupIndex) != -1) selectedGroupIndex else -1
        val preSelectedPosition = findPositionByGroupIndex(preSelectedGroupIndex)
        if (preSelectedPosition != -1) {
            notifyItemChanged(preSelectedPosition)
        }
        val selectedPosition = findPositionByGroupIndex(this.selectedGroupIndex)
        if (selectedPosition != -1) {
            notifyItemChanged(selectedPosition)
        }
    }

    fun getSelectedGroupIndex(): Int {
        return selectedGroupIndex
    }

    fun setFocusedGroupIndex(focusedGroupIndex: Int) {
        this.focusedGroupIndex = if (findPositionByGroupIndex(focusedGroupIndex) != -1) focusedGroupIndex else -1
        val focusedPosition = findPositionByGroupIndex(this.focusedGroupIndex)
        if (focusedPosition != -1) {
            notifyItemChanged(focusedPosition)
        } else {
            val selectedPosition = findPositionByGroupIndex(this.selectedGroupIndex)
            if (selectedPosition != -1) {
                notifyItemChanged(selectedPosition)
            }
        }
    }

    fun findPositionByGroupIndex(groupIndex: Int): Int {
        for (i in data.indices) {
            if (data[i].getGroupIndex() == groupIndex) return i
        }
        return -1
    }
}
