package com.fbox.mobile

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.github.tvbox.osc.R
import com.github.tvbox.osc.ui.dialog.BaseDialog
import java.util.ArrayList

object MobilePlayerControls {
    @JvmStatic
    fun install(root: View) {
        val group = root.findViewById<LinearLayout>(R.id.play_btn_group) ?: return
        val primary = listOf(R.id.play_label, R.id.play_pre, R.id.play_next, R.id.play_speed)
        val advanced = ArrayList<TextView>()
        val hidden = LinearLayout(root.context)
        hidden.visibility = View.GONE
        (root as? ViewGroup)?.addView(hidden)
        for (i in group.childCount - 1 downTo 0) {
            val child = group.getChildAt(i)
            if (child is TextView && !primary.contains(child.id)) {
                group.removeViewAt(i)
                hidden.addView(child)
                advanced.add(0, child)
            }
        }
        val episodes = MobileSheets.action(root.context, "选集")
        episodes.setOnClickListener {
            root.findViewById<View>(R.id.play_next)?.performLongClick()
        }
        group.addView(episodes)
        val more = MobileSheets.action(root.context, "更多")
        more.contentDescription = "播放设置、字幕、弹幕和投屏"
        group.addView(more)
        more.setOnClickListener {
            val dialog = BaseDialog(root.context)
            dialog.setTitle("播放设置")
            val list = LinearLayout(root.context)
            list.orientation = LinearLayout.VERTICAL
            list.setPadding(0, 0, 0, MobileSheets.dp(root.context, 16f))
            for (source in advanced) {
                if (source.visibility != View.VISIBLE) continue
                val row = createSettingRow(
                    context = root.context,
                    title = source.text.toString(),
                    isLongClickable = source.isLongClickable,
                    onAction = {
                        dialog.dismiss()
                        source.performClick()
                    },
                    onOptions = if (source.isLongClickable) {
                        {
                            dialog.dismiss()
                            source.performLongClick()
                        }
                    } else null
                )
                list.addView(row)
            }
            dialog.setContentView(list)
            dialog.show()
        }
    }

    private fun createSettingRow(
        context: Context,
        title: String,
        isLongClickable: Boolean,
        onAction: () -> Unit,
        onOptions: (() -> Unit)?
    ): View {
        val row = LinearLayout(context)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER_VERTICAL
        row.setBackgroundResource(R.drawable.mobile_setting_row_bg)
        val padH = MobileSheets.dp(context, 16f)
        val padV = MobileSheets.dp(context, 12f)
        row.setPadding(padH, padV, padH, padV)
        val rowParams = LinearLayout.LayoutParams(-1, -2)
        rowParams.topMargin = MobileSheets.dp(context, 4f)
        rowParams.bottomMargin = MobileSheets.dp(context, 4f)
        row.layoutParams = rowParams

        val titleView = TextView(context)
        titleView.text = title
        titleView.textSize = 15f
        titleView.setTextColor(0xFFEFF3F8.toInt())
        val titleParams = LinearLayout.LayoutParams(0, -2, 1f)
        row.addView(titleView, titleParams)

        if (isLongClickable && onOptions != null) {
            val pill = TextView(context)
            pill.text = "设置 ›"
            pill.textSize = 12f
            pill.setTextColor(0xFF82B1FF.toInt())
            pill.gravity = Gravity.CENTER
            pill.setPadding(
                MobileSheets.dp(context, 10f),
                MobileSheets.dp(context, 4f),
                MobileSheets.dp(context, 10f),
                MobileSheets.dp(context, 4f)
            )
            val pillBg = GradientDrawable()
            pillBg.setColor(0x331F3B66)
            pillBg.cornerRadius = MobileSheets.dp(context, 12f).toFloat()
            pillBg.setStroke(MobileSheets.dp(context, 0.8f), 0x5982B1FF.toInt())
            pill.background = pillBg
            pill.setOnClickListener { onOptions() }
            row.addView(pill)
        } else {
            val arrow = TextView(context)
            arrow.text = "›"
            arrow.textSize = 18f
            arrow.setTextColor(0xFF6B7E96.toInt())
            arrow.gravity = Gravity.CENTER
            row.addView(arrow)
        }

        row.setOnClickListener { onAction() }
        return row
    }
}
