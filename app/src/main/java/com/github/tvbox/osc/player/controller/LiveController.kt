package com.github.tvbox.osc.player.controller

import android.content.Context
import android.view.MotionEvent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.ui.compose.PlayerLiveControlView
import xyz.doikki.videoplayer.player.VideoView
import xyz.doikki.videoplayer.util.PlayerUtils

/**
 * 直播控制器
 */
class LiveController(context: Context) : BaseController(context) {
    private var minFlingDistance = 100 // 最小识别距离
    private var minFlingVelocity = 10 // 最小识别速度

    private val isLoadingState = mutableStateOf(false)
    private val isPausedState = mutableStateOf(false)
    private val isSlideSeekingState = mutableStateOf(false)
    private val slideTextState = mutableStateOf("100")
    private val pauseTextState = mutableStateOf("100")

    override fun getLayoutId(): Int {
        return 0
    }

    override fun initView() {
        super.initView()
        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    PlayerLiveControlView(
                        isLoading = isLoadingState.value,
                        isPaused = isPausedState.value,
                        pauseText = pauseTextState.value,
                        isSlideSeeking = isSlideSeekingState.value,
                        slideText = slideTextState.value,
                        onSingleTap = { },
                        onLongPress = { }
                    )
                }
            }
        }
        addView(composeView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    override fun onPlayStateChanged(playState: Int) {
        super.onPlayStateChanged(playState)
        when (playState) {
            VideoView.STATE_IDLE,
            VideoView.STATE_PLAYING,
            VideoView.STATE_PREPARED,
            VideoView.STATE_ERROR,
            VideoView.STATE_BUFFERED -> {
                isPausedState.value = false
                isLoadingState.value = false
            }
            VideoView.STATE_PAUSED -> {
                isPausedState.value = true
                isLoadingState.value = false
            }
            VideoView.STATE_PREPARING,
            VideoView.STATE_BUFFERING -> {
                isPausedState.value = false
                isLoadingState.value = true
            }
        }
        listener?.playStateChanged(playState)
    }

    override fun setProgress(duration: Int, position: Int) {
        super.setProgress(duration, position)
        pauseTextState.value = PlayerUtils.stringForTime(position) + " / " + PlayerUtils.stringForTime(duration)
    }

    interface LiveControlListener {
        fun singleTap(): Boolean
        fun longPress()
        fun playStateChanged(playState: Int)
        fun changeSource(direction: Int)
    }

    private var listener: LiveControlListener? = null

    fun setListener(listener: LiveControlListener?) {
        this.listener = listener
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        if (listener?.singleTap() == true) return true
        return super.onSingleTapConfirmed(e)
    }

    override fun onLongPress(e: MotionEvent) {
        listener?.longPress()
        super.onLongPress(e)
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        if (e1 != null) {
            if (e1.x - e2.x > minFlingDistance && Math.abs(velocityX) > minFlingVelocity) {
                listener?.changeSource(-1) // 左滑
            } else if (e2.x - e1.x > minFlingDistance && Math.abs(velocityX) > minFlingVelocity) {
                listener?.changeSource(1) // 右滑
            }
        }
        return false
    }
}
