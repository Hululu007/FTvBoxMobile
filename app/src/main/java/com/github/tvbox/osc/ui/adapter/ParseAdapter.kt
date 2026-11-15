package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.bean.ParseBean
import com.github.tvbox.osc.ui.compose.PlayParseItem
import java.util.ArrayList

class ParseAdapter : BaseQuickAdapter<ParseBean, BaseViewHolder>(0, ArrayList()) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(helper: BaseViewHolder, item: ParseBean?) {
        if (item == null) return
        val composeView = helper.itemView as ComposeView

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                PlayParseItem(
                    name = item.getName() ?: "",
                    isDefault = item.isDefault(),
                    onClick = {
                        val position = helper.adapterPosition
                        if (position in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@ParseAdapter, composeView, position)
                        }
                    }
                )
            }
        }
    }
}
