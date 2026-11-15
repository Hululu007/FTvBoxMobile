package com.github.tvbox.osc.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.tvbox.osc.bean.Epginfo
import com.github.tvbox.osc.ui.compose.EpgListItem
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class LiveEpgAdapter : BaseQuickAdapter<Epginfo, BaseViewHolder>(0, ArrayList()) {
    private var selectedEpgIndex = -1
    private var focusedEpgIndex = -1
    private var shiyiSelection = false
    private var shiyiDate: String? = null
    private var sourceIncludeBack = false

    private val timeFormat = SimpleDateFormat("yyyy-MM-dd")

    companion object {
        @JvmField
        var fontSize: Float = 20f
    }

    fun CanBack(source_include_back: Boolean?) {
        this.sourceIncludeBack = source_include_back ?: false
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return BaseViewHolder(composeView)
    }

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, value: Epginfo?) {
        if (value == null) return
        val composeView = holder.itemView as ComposeView

        val isSelected = value.index == selectedEpgIndex || value.index == focusedEpgIndex
        val isFocused = value.index == focusedEpgIndex
        val todayDate = timeFormat.format(Date())
        val isHighlightedColor = value.index == selectedEpgIndex && value.index != focusedEpgIndex && (value.currentEpgDate == shiyiDate || value.currentEpgDate == todayDate)

        val now = Date()
        val start = value.startdateTime
        val end = value.enddateTime

        var shiyiText: String? = null
        var shiyiBgColor = Color.Transparent
        var shiyiTextColor = Color.White
        var showAudioWave = false

        if (start != null && end != null && now >= start && now <= end) {
            shiyiText = "直播中"
            shiyiBgColor = Color.Yellow
            shiyiTextColor = Color.Red
        } else if (end != null && now > end && sourceIncludeBack) {
            shiyiText = "回看"
            shiyiBgColor = Color.Blue
            shiyiTextColor = Color.White
        }

        if (!shiyiSelection) {
            if (start != null && end != null && now >= start && now <= end) {
                showAudioWave = true
            }
        } else {
            if (value.index == this.selectedEpgIndex && value.currentEpgDate == shiyiDate) {
                showAudioWave = true
                shiyiText = "回看中"
                shiyiTextColor = Color.Red
                shiyiBgColor = Color(12, 255, 0)
                if (start != null && end != null && now >= start && now <= end) {
                    shiyiText = "直播中"
                    shiyiBgColor = Color.Yellow
                    shiyiTextColor = Color.Red
                }
            }
        }

        val timeText = "${value.start ?: ""} -- ${value.end ?: ""}"
        val title = value.title ?: ""

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                EpgListItem(
                    title = title,
                    timeText = timeText,
                    isSelected = isSelected,
                    isFocused = isFocused,
                    isHighlightedColor = isHighlightedColor,
                    shiyiText = shiyiText,
                    shiyiBgColor = shiyiBgColor,
                    shiyiTextColor = shiyiTextColor,
                    showAudioWave = showAudioWave,
                    onClick = {
                        val pos = holder.adapterPosition
                        if (pos in 0 until data.size) {
                            onItemClickListener?.onItemClick(this@LiveEpgAdapter, composeView, pos)
                        }
                    }
                )
            }
        }
    }

    fun setShiyiSelection(i: Int, t: Boolean, currentEpgDate: String?) {
        this.selectedEpgIndex = i
        this.shiyiDate = if (t) currentEpgDate else null
        shiyiSelection = t
        notifyItemChanged(this.selectedEpgIndex)
    }

    fun getSelectedIndex(): Int {
        return selectedEpgIndex
    }

    fun setSelectedEpgIndex(selectedEpgIndex: Int) {
        if (selectedEpgIndex == this.selectedEpgIndex) {
            if (this.selectedEpgIndex != -1) notifyItemChanged(this.selectedEpgIndex)
            return
        }
        val preSelectedEpgIndex = this.selectedEpgIndex
        this.selectedEpgIndex = selectedEpgIndex
        if (preSelectedEpgIndex != -1) {
            notifyItemChanged(preSelectedEpgIndex)
        }
        if (this.selectedEpgIndex != -1) {
            notifyItemChanged(this.selectedEpgIndex)
        }
    }

    fun getFocusedEpgIndex(): Int {
        return focusedEpgIndex
    }

    fun setFocusedEpgIndex(focusedEpgIndex: Int) {
        this.focusedEpgIndex = focusedEpgIndex
    }
}
