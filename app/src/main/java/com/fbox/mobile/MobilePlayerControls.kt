package com.fbox.mobile

import android.content.res.Configuration
import android.os.Build
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.github.tvbox.osc.R
import com.github.tvbox.osc.ui.dialog.BaseDialog

object MobilePlayerControls {
    private val primary = linkedMapOf(
        R.id.play_pre to (R.drawable.ic_action_previous to "上一集"),
        R.id.play_next to (R.drawable.ic_action_next to "下一集"),
        R.id.play_speed to (R.drawable.ic_action_speed to "播放速度"),
        R.id.danmu_setting to (R.drawable.ic_action_danmu to "弹幕设置"),
        R.id.play_cast to (R.drawable.ic_cast_device to "投屏"),
        R.id.danmu_search_ui to (R.drawable.ic_action_danmu_search to "搜索弹幕"),
        R.id.play_time_start to (R.drawable.ic_action_intro to "片头设置"),
        R.id.play_time_end to (R.drawable.ic_action_outro to "片尾设置")
    )
    private val secondary = mapOf(
        R.id.play_retry to (R.drawable.ic_action_replay to "从头播放"),
        R.id.play_refresh to (R.drawable.ic_action_refresh to "重新加载"),
        R.id.play_scale to (R.drawable.ic_action_aspect_ratio to "画面比例"),
        R.id.play_player to (R.drawable.ic_action_player to "播放器"),
        R.id.play_ijk to (R.drawable.ic_action_decoder to "解码方式"),
        R.id.play_time_reset to (R.drawable.ic_action_skip_reset to "重置片头片尾"),
        R.id.zimu_select to (R.drawable.ic_action_subtitles to "字幕设置"),
        R.id.audio_track_select to (R.drawable.ic_action_audio_track to "音轨"),
        R.id.video_track_select to (R.drawable.ic_action_video_track to "视频轨道"),
        R.id.landscape_portrait to (R.drawable.ic_action_rotate to "旋转屏幕"),
        R.id.screen_display to (R.drawable.ic_action_playback_info to "播放信息")
    )

    @JvmStatic
    fun install(root: View) {
        val group = root.findViewById<LinearLayout>(R.id.play_btn_group) ?: return
        if (root.findViewById<View>(R.id.play_more) != null) return
        val hidden = LinearLayout(root.context).apply { visibility = View.GONE }
        (root as? ViewGroup)?.addView(hidden)
        // Retain the original Views so controller updates and listeners stay connected.
        while (group.childCount > 0) {
            val child = group.getChildAt(0)
            group.removeViewAt(0)
            hidden.addView(child)
        }
        group.isBaselineAligned = false
        group.gravity = Gravity.CENTER_VERTICAL
        for ((id, action) in primary) {
            val button = root.findViewById<TextView>(id) ?: continue
            hidden.removeView(button)
            if (id == R.id.danmu_setting || id == R.id.danmu_search_ui) button.visibility = View.VISIBLE
            addAction(group, button, action.first, action.second)
            if (id == R.id.play_speed) {
                val episodes = MobileSheets.action(root.context, "选集").apply {
                    this.id = R.id.play_episode
                    setOnClickListener { root.findViewById<View>(R.id.play_next)?.performLongClick() }
                }
                addAction(group, episodes, R.drawable.ic_action_playlist, "选集")
            }
        }
        val more = MobileSheets.action(root.context, "更多").apply { id = R.id.play_more }
        addAction(group, more, R.drawable.ic_action_more, "更多播放设置")
        val visible = (0 until group.childCount).map { group.getChildAt(it) }.filter { it.visibility == View.VISIBLE }
        visible.forEachIndexed { index, view ->
            view.nextFocusLeftId = visible[(index + visible.size - 1) % visible.size].id
            view.nextFocusRightId = visible[(index + 1) % visible.size].id
        }
        more.setOnClickListener {
            val dialog = BaseDialog(root.context)
            dialog.setTitle("播放设置")
            val widthDp = root.resources.displayMetrics.widthPixels / root.resources.displayMetrics.density
            val availableWidth = minOf(widthDp, 380f) - 36f
            val columns = if (availableWidth >= 300f) 3 else 2
            val cellWidth = (availableWidth - (columns - 1) * 8f) / columns
            val grid = GridLayout(root.context).apply { columnCount = columns }
            for ((id, spec) in secondary) {
                val source = root.findViewById<TextView>(id) ?: continue
                if (source.visibility != View.VISIBLE) continue
                val value = when (id) {
                    R.id.play_scale, R.id.play_player, R.id.play_ijk -> source.text.toString()
                    R.id.landscape_portrait -> if (root.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) "当前横屏" else "当前竖屏"
                    R.id.screen_display -> if (source.isSelected) "显示中" else "已隐藏"
                    R.id.play_time_reset -> listOf(R.id.play_time_start, R.id.play_time_end).joinToString(" / ") {
                        root.findViewById<TextView>(it)?.text?.toString()?.takeIf { time -> time.contains(":") } ?: "00:00"
                    }
                    else -> null
                }
                val action = settingCard(root, spec.first, spec.second, value, source.isSelected)
                action.setOnClickListener { dialog.dismiss(); source.performClick() }
                if (source.isLongClickable) action.setOnLongClickListener {
                    dialog.dismiss()
                    source.performLongClick()
                }
                val index = grid.childCount
                grid.addView(action, GridLayout.LayoutParams().apply {
                    width = MobileSheets.dp(root.context, cellWidth)
                    height = MobileSheets.dp(root.context, 88f)
                    if (index % columns < columns - 1) marginEnd = MobileSheets.dp(root.context, 8f)
                    bottomMargin = MobileSheets.dp(root.context, 8f)
                })
            }
            dialog.setContentView(grid)
            dialog.show()
        }
    }

    private fun settingCard(root: View, icon: Int, label: String, value: String?, selected: Boolean): LinearLayout {
        val context = root.context
        val card = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(0, MobileSheets.dp(context, 6f), 0, MobileSheets.dp(context, 6f))
            setBackgroundResource(R.drawable.mobile_surface)
            isFocusable = true
            isSelected = selected
            descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
            contentDescription = if (value.isNullOrBlank()) label else "$label：$value"
            setTag(R.id.mobile_action_icon_bound, icon)
            if (Build.VERSION.SDK_INT >= 26) tooltipText = contentDescription
        }
        val tint = if (selected) 0xFFAEC6FF.toInt() else 0xFFEFF3F8.toInt()
        card.addView(ImageView(context).apply {
            setImageResource(icon)
            imageTintList = android.content.res.ColorStateList.valueOf(tint)
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        }, LinearLayout.LayoutParams(MobileSheets.dp(context, 24f), MobileSheets.dp(context, 24f)))
        card.addView(TextView(context).apply {
            text = label
            textSize = 12f
            setTextColor(tint)
            gravity = Gravity.CENTER
            setSingleLine(true)
            includeFontPadding = false
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = MobileSheets.dp(context, 2f) })
        if (!value.isNullOrBlank()) card.addView(TextView(context).apply {
            text = value
            textSize = 10f
            setTextColor(0xFFAEC6FF.toInt())
            gravity = Gravity.CENTER
            setSingleLine(true)
            ellipsize = TextUtils.TruncateAt.END
            includeFontPadding = false
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = MobileSheets.dp(context, 2f) })
        return card
    }

    private fun addAction(group: LinearLayout, button: TextView, icon: Int, label: String) {
        val caption = when (button.id) {
            R.id.play_pre -> "上集"
            R.id.play_next -> "下集"
            R.id.play_speed -> "倍速"
            R.id.play_episode -> "选集"
            R.id.danmu_setting -> "弹幕"
            R.id.play_cast -> "投屏"
            R.id.danmu_search_ui -> "搜弹幕"
            R.id.play_time_start -> "片头"
            R.id.play_time_end -> "片尾"
            else -> "更多"
        }
        MobileActions.bind(button, icon, label, caption)
        group.addView(button, LinearLayout.LayoutParams(
            MobileSheets.dp(group.context, 48f), MobileSheets.dp(group.context, 48f)
        ).apply {
            marginEnd = MobileSheets.dp(group.context, 8f)
        })
    }
}
