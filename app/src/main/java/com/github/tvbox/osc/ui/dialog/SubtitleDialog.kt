package com.github.tvbox.osc.ui.dialog

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.ui.compose.SubtitleDialogContent
import com.github.tvbox.osc.util.SubtitleHelper

class SubtitleDialog(context: Context) : BaseDialog(context) {

    @JvmField
    var selectInternal: android.view.View? = null
    var selectInternalVisible by mutableStateOf(false)
    private var exoInternalSubtitle = false

    private var sizeTextState by mutableStateOf("16")
    private var styleOneTextState by mutableStateOf("字幕样式一")
    private var styleTwoTextState by mutableStateOf("字幕样式二")
    private var styleTwoColorState by mutableStateOf(Color(0xFFFFB6C1))
    private var positionTextState by mutableStateOf("")
    private var delayHintTextState by mutableStateOf("仅对外挂字幕有效")
    private var delayTimeTextState by mutableStateOf("0")

    private var mSearchSubtitleListener: SearchSubtitleListener? = null
    private var mLocalFileChooserListener: LocalFileChooserListener? = null
    private var mSubtitleViewListener: SubtitleViewListener? = null

    init {
        sheetLayoutName = "dialog_subtitle"
        if (context is Activity) {
            setOwnerActivity(context)
        }
        selectInternal = object : android.view.View(context) {
            override fun setVisibility(visibility: Int) {
                super.setVisibility(visibility)
                selectInternalVisible = visibility == android.view.View.VISIBLE
            }
        }
        val size = SubtitleHelper.getTextSize(ownerActivity)
        sizeTextState = size.toString()

        val timeDelay = SubtitleHelper.getTimeDelay()
        if (timeDelay != 0) {
            val dbTimeDelay = timeDelay.toDouble() / 1000
            delayTimeTextState = dbTimeDelay.toString()
        }

        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    SubtitleDialogContent(
                        showInternal = selectInternalVisible,
                        onSelectInternal = {
                            dismiss()
                            mSubtitleViewListener?.selectInternalSubtitle()
                        },
                        onSelectLocal = {
                            dismiss()
                            mLocalFileChooserListener?.openLocalFileChooserDialog()
                        },
                        onSelectRemote = {
                            dismiss()
                            mSearchSubtitleListener?.openSearchSubtitleDialog()
                        },
                        sizeText = sizeTextState,
                        onSizeMinus = { onSizeMinusClick() },
                        onSizePlus = { onSizePlusClick() },
                        styleOneText = styleOneTextState,
                        styleTwoText = styleTwoTextState,
                        styleTwoColor = styleTwoColorState,
                        positionText = positionTextState,
                        onStyleOneClick = { onStyleOne() },
                        onStyleTwoClick = { onStyleTwo() },
                        delayHintText = delayHintTextState,
                        delayTimeText = delayTimeTextState,
                        onDelayMinus = { onDelayChange(-0.5) },
                        onDelayPlus = { onDelayChange(0.5) }
                    )
                }
            }
        }
        setContentView(composeView)
    }

    private fun onSizeMinusClick() {
        if (exoInternalSubtitle) {
            val scale = Math.max(50, SubtitleHelper.getExoSubtitleScale() - 5)
            sizeTextState = "$scale%"
            SubtitleHelper.setExoSubtitleScale(scale)
            mSubtitleViewListener?.setSubtitleScale(scale)
            return
        }
        var curSize = sizeTextState.toIntOrNull() ?: 0
        curSize -= 2
        if (curSize <= 12) {
            curSize = 12
        }
        sizeTextState = curSize.toString()
        SubtitleHelper.setTextSize(curSize)
        mSubtitleViewListener?.setTextSize(curSize)
    }

    private fun onSizePlusClick() {
        if (exoInternalSubtitle) {
            val scale = Math.min(200, SubtitleHelper.getExoSubtitleScale() + 5)
            sizeTextState = "$scale%"
            SubtitleHelper.setExoSubtitleScale(scale)
            mSubtitleViewListener?.setSubtitleScale(scale)
            return
        }
        var curSize = sizeTextState.toIntOrNull() ?: 0
        curSize += 2
        if (curSize >= 60) {
            curSize = 60
        }
        sizeTextState = curSize.toString()
        SubtitleHelper.setTextSize(curSize)
        mSubtitleViewListener?.setTextSize(curSize)
    }

    private fun onDelayChange(delta: Double) {
        var time = delayTimeTextState.toDoubleOrNull() ?: 0.0
        time += delta
        val newTimeStr = if (time == 0.0) "0" else time.toString()
        delayTimeTextState = newTimeStr
        val mseconds = (delta * 1000).toInt()
        SubtitleHelper.setTimeDelay((time * 1000).toInt())
        mSubtitleViewListener?.setSubtitleDelay(mseconds)
    }

    private fun onStyleOne() {
        if (exoInternalSubtitle) {
            val position = Math.min(80.0f, SubtitleHelper.getExoSubtitlePosition() + 0.5f)
            SubtitleHelper.setExoSubtitlePosition(position)
            positionTextState = if (position == 0.0f) "0" else "$position%"
            mSubtitleViewListener?.moveSubtitle(position)
            return
        }
        dismiss()
        mSubtitleViewListener?.setTextStyle(0)
        Toast.makeText(context, "设置样式成功", Toast.LENGTH_SHORT).show()
    }

    private fun onStyleTwo() {
        if (exoInternalSubtitle) {
            val position = Math.max(-80.0f, SubtitleHelper.getExoSubtitlePosition() - 0.5f)
            SubtitleHelper.setExoSubtitlePosition(position)
            positionTextState = if (position == 0.0f) "0" else "$position%"
            mSubtitleViewListener?.moveSubtitle(position)
            return
        }
        dismiss()
        mSubtitleViewListener?.setTextStyle(1)
        Toast.makeText(context, "设置样式成功", Toast.LENGTH_SHORT).show()
    }

    fun setExoInternalSubtitle(exoInternalSubtitle: Boolean) {
        this.exoInternalSubtitle = exoInternalSubtitle
        if (exoInternalSubtitle) {
            sizeTextState = "${SubtitleHelper.getExoSubtitleScale()}%"
            styleOneTextState = "字幕上移"
            styleTwoTextState = "字幕下移"
            styleTwoColorState = Color.White
            val position = SubtitleHelper.getExoSubtitlePosition()
            positionTextState = if (position == 0.0f) "0" else "$position%"
            delayHintTextState = "字幕延时对内置字幕有效"
        } else {
            sizeTextState = SubtitleHelper.getTextSize(ownerActivity).toString()
            styleOneTextState = "字幕样式一"
            styleTwoTextState = "字幕样式二"
            styleTwoColorState = Color(0xFFFFB6C1)
            positionTextState = ""
            delayHintTextState = "字幕延时仅对外挂字幕有效"
        }
    }

    fun setLocalFileChooserListener(localFileChooserListener: LocalFileChooserListener?) {
        mLocalFileChooserListener = localFileChooserListener
    }

    interface LocalFileChooserListener {
        fun openLocalFileChooserDialog()
    }

    fun setSearchSubtitleListener(searchSubtitleListener: SearchSubtitleListener?) {
        mSearchSubtitleListener = searchSubtitleListener
    }

    interface SearchSubtitleListener {
        fun openSearchSubtitleDialog()
    }

    fun setSubtitleViewListener(subtitleViewListener: SubtitleViewListener?) {
        mSubtitleViewListener = subtitleViewListener
    }

    interface SubtitleViewListener {
        fun setTextSize(size: Int)
        fun setSubtitleDelay(milliseconds: Int)
        fun selectInternalSubtitle()
        fun setTextStyle(style: Int)
        fun setSubtitleScale(scale: Int)
        fun moveSubtitle(offset: Float)
    }
}
