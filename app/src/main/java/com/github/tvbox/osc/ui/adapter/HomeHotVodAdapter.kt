package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.api.ApiConfig
import com.github.tvbox.osc.bean.Movie
import com.github.tvbox.osc.ui.compose.UserHotVodItem
import com.github.tvbox.osc.util.HawkConfig
import com.github.tvbox.osc.util.ImgUtil
import com.orhanobut.hawk.Hawk
import java.util.ArrayList

class HomeHotVodAdapter(private val style: ImgUtil.Style?, private var tvRateValue: String?) :
    BaseQuickAdapter<Movie.Video, BaseViewHolder>(0, ArrayList()) {

    private var defaultWidth: Int = 0
    private var phoneListMode = false

    init {
        if (style != null) {
            this.defaultWidth = ImgUtil.getStyleDefaultWidth(style)
        }
    }

    fun setPhoneListMode(value: Boolean) {
        if (phoneListMode == value) return
        phoneListMode = value
        notifyDataSetChanged()
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
        val isDeleteMode = HawkConfig.hotVodDelete

        var rateText = tvRateValue ?: ""
        if (Hawk.get(HawkConfig.HOME_REC, HawkConfig.DEFAULT_HOME_REC) == 2) {
            val bean = ApiConfig.get().getSource(item.sourceKey)
            rateText = bean?.name ?: ""
        }

        val composeView = helper.itemView as ComposeView

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                UserHotVodItem(
                    name = displayName,
                    rate = rateText,
                    note = displayNote,
                    pic = item.pic,
                    isPhoneListMode = phoneListMode,
                    isDeleteMode = isDeleteMode,
                    onClick = {
                        val position = helper.adapterPosition
                        if (position in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@HomeHotVodAdapter, composeView, position)
                        }
                    },
                    onDeleteClick = {
                        val position = helper.adapterPosition
                        if (position in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@HomeHotVodAdapter, composeView, position)
                        }
                    }
                )
            }
        }
    }
}
