package com.github.tvbox.osc.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.tvbox.osc.bean.Epginfo
import com.github.tvbox.osc.ui.compose.EpgListItem

class MyEpgAdapter(private val data: List<Epginfo>, private val context: Context, i: Int) : BaseAdapter() {
    private var defaultSelection = i
    private var defaultShiyiSelection = 0

    companion object {
        @JvmField
        var fontSize: Float = 20f
    }

    fun setSelection(i: Int) {
        this.defaultSelection = i
        notifyDataSetChanged()
    }

    fun setShiyiSelection(i: Int) {
        this.defaultShiyiSelection = i
        notifyDataSetChanged()
    }

    fun setFontSize(f: Float) {
        fontSize = f
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View {
        val composeView = (convertView as? ComposeView) ?: ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }

        if (i < data.size) {
            val item = data[i]
            val title = item.title ?: ""
            val timeText = "${item.start ?: ""} -- ${item.end ?: ""}"
            val isSelected = i == this.defaultSelection
            val isHighlighted = isSelected

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
                        isHighlightedColor = isHighlighted,
                        showAudioWave = isSelected
                    )
                }
            }
        }
        return composeView
    }
}
