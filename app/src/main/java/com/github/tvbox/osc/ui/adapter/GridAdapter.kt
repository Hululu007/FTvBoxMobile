package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.bean.Movie
import com.github.tvbox.osc.ui.compose.AppListItem
import com.github.tvbox.osc.ui.compose.GridItem
import com.github.tvbox.osc.util.ImgUtil
import java.util.ArrayList

class GridAdapter(showList: Boolean, @JvmField var style: ImgUtil.Style?) :
    BaseQuickAdapter<Movie.Video, BaseViewHolder>(0, ArrayList()) {
    private var mShowList: Boolean = showList
    private var defaultWidth: Int = 0

    init {
        val s = style
        if (s != null) {
            if (s.type == "list") this.mShowList = true
            this.defaultWidth = ImgUtil.getStyleDefaultWidth(s)
        }
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    override fun convert(helper: BaseViewHolder, item: Movie.Video?) {
        if (item == null) return
        val displayName = com.fbox.mobile.auth.MobileConfigAuth.title(item)
        val displayNote = com.fbox.mobile.auth.MobileConfigAuth.note(item)
        val composeView = helper.itemView as ComposeView

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                if (mShowList) {
                    AppListItem(
                        name = displayName ?: "",
                        note = displayNote ?: "",
                        pic = item.pic,
                        onClick = {
                            val position = helper.adapterPosition
                            if (position in 0 until data.size) {
                                onItemClickListener?.onItemClick(this@GridAdapter, composeView, position)
                            }
                        }
                    )
                } else {
                    val yearStr = if (item.year > 0) item.year.toString() else null
                    GridItem(
                        name = displayName,
                        year = yearStr,
                        note = displayNote,
                        pic = item.pic,
                        onClick = {
                            val position = helper.adapterPosition
                            if (position in 0 until data.size) {
                                onItemClickListener?.onItemClick(this@GridAdapter, composeView, position)
                            }
                        }
                    )
                }
            }
        }
    }
}
