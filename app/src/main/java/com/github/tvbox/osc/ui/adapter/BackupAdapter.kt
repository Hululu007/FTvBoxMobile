package com.github.tvbox.osc.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.R
import com.github.tvbox.osc.ui.compose.DialogBackupItem
import java.util.ArrayList

class BackupAdapter : BaseQuickAdapter<String, BaseViewHolder>(0, ArrayList()) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(helper: BaseViewHolder, item: String?) {
        val name = item ?: ""
        val composeView = helper.itemView as ComposeView

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                DialogBackupItem(
                    name = name,
                    onNameClick = {
                        val position = helper.adapterPosition
                        if (position in 0 until data.size) {
                            val dummyView = View(composeView.context).apply { id = R.id.tvName }
                            onItemChildClickListener?.onItemChildClick(this@BackupAdapter, dummyView, position)
                        }
                    },
                    onDeleteClick = {
                        val position = helper.adapterPosition
                        if (position in 0 until data.size) {
                            val dummyView = View(composeView.context).apply { id = R.id.tvDel }
                            onItemChildClickListener?.onItemChildClick(this@BackupAdapter, dummyView, position)
                        }
                    }
                )
            }
        }
    }
}
