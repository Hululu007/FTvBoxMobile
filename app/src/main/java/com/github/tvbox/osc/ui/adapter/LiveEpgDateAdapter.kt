package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.bean.LiveEpgDate
import com.github.tvbox.osc.ui.compose.LiveChannelGroupItem
import java.util.ArrayList

class LiveEpgDateAdapter : BaseQuickAdapter<LiveEpgDate, BaseViewHolder>(0, ArrayList()) {

    private var selectedIndex = -1
    private var focusedIndex = -1

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(holder: BaseViewHolder, item: LiveEpgDate?) {
        if (item == null) return
        val composeView = holder.itemView as ComposeView
        val itemIndex = item.getIndex()
        val isSelected = itemIndex == selectedIndex
        val isFocused = itemIndex == focusedIndex

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                LiveChannelGroupItem(
                    groupName = item.getDatePresented() ?: "",
                    isSelected = isSelected,
                    isFocused = isFocused,
                    isEpgDateMode = true,
                    onClick = {
                        val pos = holder.adapterPosition
                        if (pos in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@LiveEpgDateAdapter, composeView, pos)
                        }
                    }
                )
            }
        }
    }

    fun setSelectedIndex(selectedIndex: Int) {
        if (selectedIndex == this.selectedIndex) return
        val preSelectedIndex = this.selectedIndex
        this.selectedIndex = selectedIndex
        if (preSelectedIndex != -1) {
            notifyItemChanged(preSelectedIndex)
        }
        if (this.selectedIndex != -1) {
            notifyItemChanged(this.selectedIndex)
        }
    }

    fun getSelectedIndex(): Int {
        return selectedIndex
    }

    fun setFocusedIndex(focusedIndex: Int) {
        val preSelectedIndex = this.selectedIndex
        this.focusedIndex = focusedIndex
        if (preSelectedIndex != -1) {
            notifyItemChanged(preSelectedIndex)
        }
        if (this.focusedIndex != -1) {
            notifyItemChanged(this.focusedIndex)
        }
    }
}
