package com.fbox.mobile

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.text.method.TransformationMethod
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.TooltipCompat
import androidx.core.widget.TextViewCompat
import com.github.tvbox.osc.R

/** Compact actions keep the original View, text state and controller listeners. */
object MobileActions {
    private object IconOnly : TransformationMethod {
        override fun getTransformation(source: CharSequence?, view: View?): CharSequence = ""
        override fun onFocusChanged(view: View?, sourceText: CharSequence?, focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {}
    }

    private class IconCaption(private val caption: String) : TransformationMethod {
        override fun getTransformation(source: CharSequence?, view: View?): CharSequence {
            if (view?.id == R.id.play_speed) {
                val speed = source?.toString()?.removeSuffix("x")?.removeSuffix("×")?.toFloatOrNull()
                if (speed != null) return (if (speed % 1f == 0f) speed.toInt().toString() else speed.toString()) + "×"
            }
            return caption
        }
        override fun onFocusChanged(view: View?, sourceText: CharSequence?, focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {}
    }

    fun bind(button: TextView, icon: Int, label: String, caption: String? = null) {
        if (button.getTag(R.id.mobile_action_icon_bound) != null) return
        button.setTag(R.id.mobile_action_icon_bound, icon)
        val size = MobileSheets.dp(button.context, 48f)
        button.minWidth = size
        button.minimumWidth = size
        button.minHeight = size
        button.minimumHeight = size
        val iconSize = MobileSheets.dp(button.context, if (caption == null) 24f else 22f)
        val iconPadding = if (caption == null) (size - iconSize) / 2 else 0
        button.setPaddingRelative(iconPadding, 0, iconPadding, 0)
        button.gravity = Gravity.CENTER
        button.textAlignment = View.TEXT_ALIGNMENT_GRAVITY
        button.setSingleLine(true)
        button.transformationMethod = if (caption == null) IconOnly else IconCaption(caption)
        if (caption != null) {
            button.textSize = 10f
            button.includeFontPadding = false
            button.setLineSpacing(0f, 1f)
        }
        val drawable = AppCompatResources.getDrawable(button.context, icon)?.mutate()
        drawable?.setBounds(0, 0, iconSize, iconSize)
        if (caption == null) button.setCompoundDrawablesRelative(drawable, null, null, null)
        else button.setCompoundDrawablesRelative(null, drawable, null, null)
        button.compoundDrawablePadding = if (caption == null) 0 else MobileSheets.dp(button.context, 2f)
        if (caption != null) {
            centerCaption(button)
            button.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> centerCaption(button) }
        }
        TextViewCompat.setCompoundDrawableTintList(button, android.content.res.ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf()),
            intArrayOf(0xFFAEC6FF.toInt(), button.currentTextColor)
        ))
        button.layoutParams?.let { params ->
            params.width = size
            params.height = size
            if (params is LinearLayout.LayoutParams) params.weight = 0f
            button.layoutParams = params
        }
        fun describe() {
            val state = button.text.toString().trim()
            when (button.id) {
                R.id.tvCollect -> button.isSelected = state.contains("取消") || state.contains("已收藏")
                R.id.play_time_start, R.id.play_time_end ->
                    button.isSelected = state.contains(":") && state != "00:00" && state != "00:00:00"
            }
            val description = if (state.isEmpty() || state == label) label else "$label：$state"
            button.contentDescription = description
            if (Build.VERSION.SDK_INT >= 26) button.tooltipText = description
            else if (!button.isLongClickable) TooltipCompat.setTooltipText(button, description)
        }
        button.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { describe() }
        })
        describe()
    }

    private fun centerCaption(button: TextView) {
        val icon = button.compoundDrawablesRelative[1] ?: return
        val metrics = button.paint.fontMetricsInt
        val textHeight = button.layout?.height ?: (metrics.descent - metrics.ascent)
        val height = button.height.takeIf { it > 0 } ?: MobileSheets.dp(button.context, 48f)
        // TextView pins its top drawable to paddingTop, independently of text gravity.
        val inset = maxOf(0, (height - icon.bounds.height() - button.compoundDrawablePadding - textHeight) / 2)
        if (button.paddingTop != inset || button.paddingBottom != inset) {
            button.setPaddingRelative(0, inset, 0, inset)
        }
    }

    private fun bind(root: View, id: Int, icon: Int, label: String, caption: String? = null) {
        root.findViewById<TextView>(id)?.let { bind(it, icon, label, caption) }
    }

    fun installScreen(root: View) {
        if (root.findViewById<View>(R.id.tvChangeSource) != null) {
            bind(root, R.id.tvPlay, R.drawable.ic_action_play, "播放", "播放")
            bind(root, R.id.tvQuickSearch, R.drawable.icon_search, "快速搜索", "快搜")
            bind(root, R.id.tvDesc, R.drawable.ic_action_description, "影片简介", "简介")
            bind(root, R.id.tvCollect, R.drawable.ic_action_favorite, "收藏", "收藏")
            bind(root, R.id.tvChangeSource, R.drawable.ic_action_source, "切换来源", "切源")
            bind(root, R.id.mSeriesSortTv, R.drawable.ic_action_sort, "剧集排序", "排序")
            val ids = listOf(R.id.tvPlay, R.id.tvQuickSearch, R.id.tvDesc, R.id.tvCollect, R.id.tvChangeSource)
            val buttons = ids.mapNotNull { root.findViewById<TextView>(it) }
            val row = buttons.firstOrNull()?.parent as? LinearLayout
            if (row != null) {
                row.isBaselineAligned = false
                for (button in buttons) {
                    val oldRow = button.parent as? ViewGroup
                    if (oldRow !== row) {
                        oldRow?.removeView(button)
                        row.addView(button)
                        if (oldRow != null && oldRow.childCount == 0) (oldRow.parent as? ViewGroup)?.removeView(oldRow)
                    }
                    button.layoutParams = LinearLayout.LayoutParams(
                        MobileSheets.dp(root.context, 48f), MobileSheets.dp(root.context, 48f)
                    ).apply { marginEnd = MobileSheets.dp(root.context, 8f) }
                }
            }
        }
        if (root.findViewById<View>(R.id.etSearch) != null) {
            bind(root, R.id.tvSearch, R.drawable.icon_search, "搜索")
            bind(root, R.id.tvClear, R.drawable.ic_sheet_close, "清空输入")
            bind(root, R.id.tvSearchCheckboxBtn, R.drawable.icon_filter, "选择搜索数据源")
        }
        bind(root, R.id.pushLocal, R.drawable.ic_action_paste, "粘贴链接")
        bind(root, R.id.mobilePlayLink, R.drawable.ic_action_play, "播放链接")
    }

    fun installDialog(root: View, layout: String) {
        when (layout) {
            "dialog_api" -> {
                bind(root, R.id.mobilePasteApi, R.drawable.ic_action_paste, "粘贴点播地址")
                bind(root, R.id.mobilePasteLive, R.drawable.ic_action_paste, "粘贴直播地址")
                bind(root, R.id.inputSubmit, R.drawable.ic_action_folder, "选择点播配置文件")
                bind(root, R.id.inputSubmitLive, R.drawable.ic_action_folder, "选择直播配置文件")
                bind(root, R.id.apiHistory, R.drawable.icon_history, "直播配置历史")
                bind(root, R.id.mobileCancelApi, R.drawable.ic_sheet_close, "取消")
                bind(root, R.id.inputConfirm, R.drawable.ic_action_save, "保存并更新")
            }
            "dialog_confirm" -> {
                bind(root, R.id.btnCancel, R.drawable.ic_sheet_close, "取消")
                bind(root, R.id.btnConfirm, R.drawable.icon_delete, "清空")
            }
            "dialog_search_danmu", "dialog_search_subtitle", "dialog_search_remotetv" ->
                bind(root, R.id.inputSubmit, R.drawable.icon_search, "搜索")
            "dialog_subtitle" -> {
                bind(root, R.id.selectInternal, R.drawable.ic_action_subtitles, "内置字幕", "内置")
                bind(root, R.id.selectLocal, R.drawable.ic_action_folder, "本地字幕", "本地")
                bind(root, R.id.selectRemote, R.drawable.ic_action_subtitle_search, "在线搜索字幕", "搜字幕")
            }
            "dialog_danmu_setting" ->
                bind(root, R.id.danmuSearch, R.drawable.ic_action_danmu_search, "搜索在线弹幕")
        }
    }
}
