package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.ui.compose.LocalFileItem
import java.io.File
import java.text.DecimalFormat
import java.util.ArrayList

class LocalFileAdapter : BaseQuickAdapter<File, BaseViewHolder>(0, ArrayList()) {
    companion object {
        private val SIZE_FORMAT = DecimalFormat("0.#")
    }

    private var parentDir: File? = null

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(helper: BaseViewHolder, item: File?) {
        if (item == null) return
        val isParent = parentDir != null && parentDir == item
        val typeText = if (item.isDirectory) "目录" else "文件"
        val nameText = if (isParent) ".." else item.name
        val infoText = if (item.isDirectory) "进入" else formatSize(item.length())
        val composeView = helper.itemView as ComposeView

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                LocalFileItem(
                    typeText = typeText,
                    name = nameText,
                    info = infoText,
                    onClick = {
                        val position = helper.adapterPosition
                        if (position in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@LocalFileAdapter, composeView, position)
                        }
                    }
                )
            }
        }
    }

    fun setParentDir(parentDir: File?) {
        this.parentDir = parentDir
    }

    private fun formatSize(size: Long): String {
        if (size < 1024) return "$size B"
        val kb = size / 1024.0
        if (kb < 1024) return SIZE_FORMAT.format(kb) + " KB"
        val mb = kb / 1024.0
        if (mb < 1024) return SIZE_FORMAT.format(mb) + " MB"
        return SIZE_FORMAT.format(mb / 1024.0) + " GB"
    }
}
