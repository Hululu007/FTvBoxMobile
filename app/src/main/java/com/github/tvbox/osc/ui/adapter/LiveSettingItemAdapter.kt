package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.bean.LiveSettingItem
import com.github.tvbox.osc.ui.compose.LiveSettingItem
import java.util.ArrayList

class LiveSettingItemAdapter : BaseQuickAdapter<LiveSettingItem, BaseViewHolder>(0, ArrayList()) {
    private var focusedItemIndex = -1

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(holder: BaseViewHolder, item: LiveSettingItem?) {
        if (item == null) return
        val composeView = holder.itemView as ComposeView
        val itemIndex = item.getItemIndex()
        val isSelected = item.isItemSelected()
        val isFocused = itemIndex == focusedItemIndex

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                LiveSettingItem(
                    itemName = item.getItemName() ?: "",
                    isSelected = isSelected,
                    isFocused = isFocused,
                    onClick = {
                        val pos = holder.adapterPosition
                        if (pos in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@LiveSettingItemAdapter, composeView, pos)
                        }
                    }
                )
            }
        }
    }

    fun selectItem(selectedItemIndex: Int, select: Boolean, unselectPreItemIndex: Boolean) {
        if (unselectPreItemIndex) {
            val preSelectedItemIndex = getSelectedItemIndex()
            if (preSelectedItemIndex != -1) {
                data[preSelectedItemIndex].setItemSelected(false)
                notifyItemChanged(preSelectedItemIndex)
            }
        }
        if (selectedItemIndex != -1 && selectedItemIndex in data.indices) {
            data[selectedItemIndex].setItemSelected(select)
            notifyItemChanged(selectedItemIndex)
        }
    }

    fun setFocusedItemIndex(focusedItemIndex: Int) {
        val preFocusItemIndex = this.focusedItemIndex
        this.focusedItemIndex = focusedItemIndex
        if (preFocusItemIndex != -1) {
            notifyItemChanged(preFocusItemIndex)
        }
        if (this.focusedItemIndex != -1) {
            notifyItemChanged(this.focusedItemIndex)
        }
    }

    fun getSelectedItemIndex(): Int {
        for (item in data) {
            if (item.isItemSelected()) return item.getItemIndex()
        }
        return -1
    }
}
