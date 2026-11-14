package com.fbox.mobile

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.github.tvbox.osc.R
import com.github.tvbox.osc.ui.dialog.BaseDialog
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

object MobileSheets {
    @JvmStatic
    fun dp(context: Context, value: Float): Int {
        return (value * context.resources.displayMetrics.density).roundToInt()
    }

    @JvmStatic
    fun install(dialog: BaseDialog) {
        val context = dialog.context
        val window = dialog.window ?: return
        val host = dialog.findViewById<ViewGroup>(android.R.id.content) ?: return
        if (host.childCount == 0) return
        val body = host.getChildAt(0)

        val sheetTitle = dialog.sheetTitle ?: dialog.getSheetTitle()
        val input = body.findViewById<EditText>(R.id.input)
        val submit = body.findViewById<View>(R.id.inputSubmit)
        if (input != null && submit != null && body.findViewById<View>(R.id.mobileApiActions) == null) {
            input.setOnEditorActionListener { _, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH || action == EditorInfo.IME_ACTION_DONE || action == EditorInfo.IME_ACTION_GO) {
                    hideKeyboard(input)
                    submit.performClick()
                    true
                } else {
                    false
                }
            }
        }
        val footer = body.findViewById<View>(R.id.mobileApiActions)
        if (footer != null) {
            (footer.parent as? ViewGroup)?.removeView(footer)
        }
        host.removeView(body)

        val metrics = context.resources.displayMetrics
        val portrait = metrics.heightPixels > metrics.widthPixels

        val sheet = object : LinearLayout(context) {
            override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
                if (portrait) {
                    var available = (resources.displayMetrics.heightPixels * 0.85f).toInt()
                    if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED) {
                        available = min(available, MeasureSpec.getSize(heightMeasureSpec))
                    }
                    super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(available, MeasureSpec.AT_MOST))
                } else {
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                }
            }
        }
        sheet.orientation = LinearLayout.VERTICAL
        sheet.setPadding(dp(context, 18f), dp(context, if (portrait) 6f else 14f), dp(context, 18f), dp(context, 16f))
        sheet.setBackgroundResource(if (portrait) R.drawable.mobile_sheet_port else R.drawable.mobile_sheet_land)

        if (portrait) {
            val handleArea = FrameLayout(context)
            handleArea.contentDescription = "下滑关闭弹窗"
            val handle = View(context)
            val handleShape = GradientDrawable()
            handleShape.setColor(0x4DFFFFFF)
            handleShape.cornerRadius = dp(context, 2f).toFloat()
            handle.background = handleShape
            handleArea.addView(handle, FrameLayout.LayoutParams(dp(context, 36f), dp(context, 4f), Gravity.CENTER))
            sheet.addView(handleArea, LinearLayout.LayoutParams(-1, dp(context, 18f)))
            installVerticalDragDismiss(handleArea, sheet, dialog)
        }

        val header = LinearLayout(context)
        header.orientation = LinearLayout.HORIZONTAL
        header.gravity = Gravity.CENTER_VERTICAL
        val headerHeight = dp(context, 48f)

        val title = TextView(context)
        title.text = sheetTitle
        title.textSize = 17f
        title.typeface = Typeface.DEFAULT_BOLD
        title.setTextColor(0xFFF0F4FA.toInt())
        title.maxLines = 1
        title.ellipsize = TextUtils.TruncateAt.END
        val titleParams = LinearLayout.LayoutParams(0, -2, 1f)
        header.addView(title, titleParams)

        val closeFrame = FrameLayout(context)
        closeFrame.contentDescription = "关闭弹窗"
        val closeVisual = FrameLayout(context)
        closeVisual.setBackgroundResource(R.drawable.mobile_close_btn_bg)
        val closeIcon = ImageView(context)
        closeIcon.setImageResource(R.drawable.ic_sheet_close)
        val iconSize = dp(context, 18f)
        val iconParams = FrameLayout.LayoutParams(iconSize, iconSize, Gravity.CENTER)
        closeVisual.addView(closeIcon, iconParams)
        val closeSize = dp(context, 34f)
        closeFrame.addView(closeVisual, FrameLayout.LayoutParams(closeSize, closeSize, Gravity.CENTER))
        closeFrame.setOnClickListener { dialog.cancel() }
        val closeTouchSize = dp(context, 48f)
        header.addView(closeFrame, LinearLayout.LayoutParams(closeTouchSize, closeTouchSize))

        sheet.addView(header, LinearLayout.LayoutParams(-1, headerHeight))

        val divider = View(context)
        divider.setBackgroundColor(0x14FFFFFF)
        val divParams = LinearLayout.LayoutParams(-1, dp(context, 0.8f))
        divParams.topMargin = dp(context, 6f)
        divParams.bottomMargin = dp(context, 10f)
        sheet.addView(divider, divParams)

        if (portrait) {
            installVerticalDragDismiss(header, sheet, dialog)
        } else {
            installHorizontalDragDismiss(header, sheet, dialog)
        }

        val scroll = NestedScrollView(context)
        scroll.isFillViewport = true
        scroll.clipToPadding = false
        body.setBackgroundColor(Color.TRANSPARENT)
        scroll.addView(body, FrameLayout.LayoutParams(-1, -1))
        val bodyParams = if (portrait) {
            LinearLayout.LayoutParams(-1, -2, 1f)
        } else {
            LinearLayout.LayoutParams(-1, 0, 1f)
        }
        sheet.addView(scroll, bodyParams)

        if (footer != null) {
            val footerParams = LinearLayout.LayoutParams(-1, -2)
            footerParams.topMargin = dp(context, 10f)
            sheet.addView(footer, footerParams)
        }

        val sheetHeight = if (portrait) ViewGroup.LayoutParams.WRAP_CONTENT else ViewGroup.LayoutParams.MATCH_PARENT
        host.addView(sheet, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, sheetHeight))

        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        val attrs = window.attributes
        attrs.dimAmount = 0.42f
        window.attributes = attrs
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        dialog.setCanceledOnTouchOutside(true)
    }

    private fun installVerticalDragDismiss(touchArea: View, sheet: View, dialog: BaseDialog) {
        touchArea.setOnTouchListener(object : View.OnTouchListener {
            var startY = 0f
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    startY = event.rawY
                    return true
                }
                val distance = max(0f, event.rawY - startY)
                if (event.actionMasked == MotionEvent.ACTION_MOVE) {
                    sheet.translationY = distance
                    return true
                }
                if (event.actionMasked == MotionEvent.ACTION_UP) {
                    if (distance >= dp(view.context, 64f)) {
                        dialog.cancel()
                    } else {
                        sheet.animate().translationY(0f).setDuration(150).start()
                    }
                    return true
                }
                if (event.actionMasked == MotionEvent.ACTION_CANCEL) {
                    sheet.translationY = 0f
                }
                return true
            }
        })
    }

    private fun installHorizontalDragDismiss(touchArea: View, sheet: View, dialog: BaseDialog) {
        touchArea.setOnTouchListener(object : View.OnTouchListener {
            var startX = 0f
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    startX = event.rawX
                    return true
                }
                val distance = max(0f, event.rawX - startX)
                if (event.actionMasked == MotionEvent.ACTION_MOVE) {
                    sheet.translationX = distance
                    return true
                }
                if (event.actionMasked == MotionEvent.ACTION_UP) {
                    if (distance >= dp(view.context, 64f)) {
                        dialog.cancel()
                    } else {
                        sheet.animate().translationX(0f).setDuration(150).start()
                    }
                    return true
                }
                if (event.actionMasked == MotionEvent.ACTION_CANCEL) {
                    sheet.translationX = 0f
                }
                return true
            }
        })
    }

    @JvmStatic
    fun action(context: Context, label: String): TextView {
        val button = TextView(context)
        button.text = label
        button.setTextColor(0xFFEFF3F8.toInt())
        button.textSize = 14f
        button.gravity = Gravity.CENTER
        button.setPadding(dp(context, 14f), dp(context, 6f), dp(context, 14f), dp(context, 6f))
        button.minHeight = dp(context, 48f)
        button.setBackgroundResource(R.drawable.mobile_surface)
        return button
    }

    @JvmStatic
    fun hideKeyboard(input: View) {
        val keyboard = input.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        keyboard?.hideSoftInputFromWindow(input.windowToken, 0)
    }
}
