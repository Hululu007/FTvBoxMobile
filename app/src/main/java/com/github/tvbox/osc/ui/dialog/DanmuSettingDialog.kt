package com.github.tvbox.osc.ui.dialog

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.event.RefreshEvent
import com.github.tvbox.osc.ui.compose.DanmuSettingDialogContent
import com.github.tvbox.osc.util.DanmuHelper
import org.greenrobot.eventbus.EventBus

class DanmuSettingDialog(context: Context) : BaseDialog(context) {
    private var danmuSearchListener: DanmuSearchListener? = null

    private var speedState by mutableStateOf(1.5f)
    private var sizeScaleState by mutableStateOf(1.0f)
    private var maxLineState by mutableStateOf(8)
    private var alphaState by mutableStateOf(1.0f)
    private var useRandomColorState by mutableStateOf(false)

    init {
        sheetLayoutName = "dialog_danmu_setting"
        speedState = DanmuHelper.getSpeed()
        sizeScaleState = DanmuHelper.getSizeScale()
        maxLineState = DanmuHelper.getMaxLine()
        alphaState = DanmuHelper.getAlpha()
        useRandomColorState = DanmuHelper.useRandomColor()

        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    DanmuSettingDialogContent(
                        onSearchDanmuClick = {
                            val listener = danmuSearchListener ?: return@DanmuSettingDialogContent
                            dismiss()
                            listener.openSearchDanmuDialog()
                        },
                        speed = speedState,
                        onSpeedChange = { newSpeed ->
                            speedState = newSpeed
                            DanmuHelper.setSpeed(newSpeed)
                            EventBus.getDefault().post(RefreshEvent(RefreshEvent.TYPE_SET_DANMU_SETTINGS, false))
                        },
                        sizeScale = sizeScaleState,
                        onSizeChange = { newSize ->
                            sizeScaleState = newSize
                            DanmuHelper.setSizeScale(newSize)
                            EventBus.getDefault().post(RefreshEvent(RefreshEvent.TYPE_SET_DANMU_SETTINGS, false))
                        },
                        maxLine = maxLineState,
                        onLineChange = { newLine ->
                            maxLineState = newLine
                            DanmuHelper.setMaxLine(newLine)
                            EventBus.getDefault().post(RefreshEvent(RefreshEvent.TYPE_SET_DANMU_SETTINGS, false))
                        },
                        alpha = alphaState,
                        onAlphaChange = { newAlpha ->
                            alphaState = newAlpha
                            DanmuHelper.setAlpha(newAlpha)
                            EventBus.getDefault().post(RefreshEvent(RefreshEvent.TYPE_SET_DANMU_SETTINGS, false))
                        },
                        useRandomColor = useRandomColorState,
                        onColorChange = { newColor ->
                            useRandomColorState = newColor
                            DanmuHelper.setRandomColor(newColor)
                            EventBus.getDefault().post(RefreshEvent(RefreshEvent.TYPE_SET_DANMU_SETTINGS, true))
                        }
                    )
                }
            }
        }
        setContentView(composeView)
    }

    fun setDanmuSearchListener(danmuSearchListener: DanmuSearchListener?) {
        this.danmuSearchListener = danmuSearchListener
    }

    interface DanmuSearchListener {
        fun openSearchDanmuDialog()
    }
}
