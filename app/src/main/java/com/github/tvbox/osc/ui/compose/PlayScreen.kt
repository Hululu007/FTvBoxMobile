package com.github.tvbox.osc.ui.compose

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.tvbox.osc.R
import com.github.tvbox.osc.player.MyVideoView
import master.flame.danmaku.ui.widget.DanmakuView

@Composable
fun PlayScreen(
    videoView: MyVideoView? = null,
    danmakuView: DanmakuView? = null,
    loadingView: ProgressBar? = null,
    errorView: ImageView? = null,
    tipView: TextView? = null,
    onSetupVideoView: ((MyVideoView) -> Unit)? = null,
    onSetupDanmaku: ((DanmakuView) -> Unit)? = null,
    onSetupLoading: ((ProgressBar) -> Unit)? = null,
    onSetupError: ((ImageView) -> Unit)? = null,
    onSetupTip: ((TextView) -> Unit)? = null
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFAEC6FF),
            background = Color.Black,
            surface = Color(0xFF171C26),
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            if (videoView != null) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = {
                        (videoView.parent as? android.view.ViewGroup)?.removeView(videoView)
                        videoView
                    }
                )
            } else {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        MyVideoView(ctx).apply {
                            id = R.id.mVideoView
                            onSetupVideoView?.invoke(this)
                        }
                    }
                )
            }

            if (danmakuView != null) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = {
                        (danmakuView.parent as? android.view.ViewGroup)?.removeView(danmakuView)
                        danmakuView
                    }
                )
            } else {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        DanmakuView(ctx).apply {
                            id = R.id.danmaku
                            visibility = View.GONE
                            onSetupDanmaku?.invoke(this)
                        }
                    }
                )
            }

            if (loadingView != null) {
                AndroidView(
                    modifier = Modifier.size(36.dp),
                    factory = {
                        (loadingView.parent as? android.view.ViewGroup)?.removeView(loadingView)
                        loadingView
                    }
                )
            } else {
                AndroidView(
                    modifier = Modifier.size(36.dp),
                    factory = { ctx ->
                        ProgressBar(ctx).apply {
                            id = R.id.play_loading
                            onSetupLoading?.invoke(this)
                        }
                    }
                )
            }

            if (errorView != null) {
                AndroidView(
                    modifier = Modifier.size(36.dp),
                    factory = {
                        (errorView.parent as? android.view.ViewGroup)?.removeView(errorView)
                        errorView
                    }
                )
            } else {
                AndroidView(
                    modifier = Modifier.size(36.dp),
                    factory = { ctx ->
                        ImageView(ctx).apply {
                            id = R.id.play_load_error
                            setImageResource(R.drawable.icon_error)
                            visibility = View.GONE
                            onSetupError?.invoke(this)
                        }
                    }
                )
            }

            if (tipView != null) {
                AndroidView(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 70.dp),
                    factory = {
                        (tipView.parent as? android.view.ViewGroup)?.removeView(tipView)
                        tipView
                    }
                )
            } else {
                AndroidView(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 70.dp),
                    factory = { ctx ->
                        TextView(ctx).apply {
                            id = R.id.play_load_tip
                            setTextColor(android.graphics.Color.parseColor("#CCFFFFFF"))
                            textSize = 15f
                            onSetupTip?.invoke(this)
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun PlayScreenPreview() {
    PlayScreen()
}
