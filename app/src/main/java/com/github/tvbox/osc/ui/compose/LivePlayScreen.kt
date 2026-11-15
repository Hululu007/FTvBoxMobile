package com.github.tvbox.osc.ui.compose

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun LivePlayScreen(
    videoView: View? = null,
    overlayView: View? = null,
    channelTextView: View? = null,
    leftChannelListLayout: View? = null,
    rightSettingLayout: View? = null,
    statusInfoView: View? = null,
    loadingView: View? = null,
    rightTopHuikanView: View? = null,
    backControllerView: View? = null
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
                .background(Color.Black)
        ) {
            if (videoView != null) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = {
                        (videoView.parent as? android.view.ViewGroup)?.removeView(videoView)
                        videoView
                    }
                )
            }

            if (overlayView != null) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = {
                        (overlayView.parent as? android.view.ViewGroup)?.removeView(overlayView)
                        overlayView
                    }
                )
            }

            if (channelTextView != null) {
                AndroidView(
                    modifier = Modifier.wrapContentSize(),
                    factory = {
                        (channelTextView.parent as? android.view.ViewGroup)?.removeView(channelTextView)
                        channelTextView
                    }
                )
            }

            if (leftChannelListLayout != null) {
                AndroidView(
                    modifier = Modifier.fillMaxHeight(),
                    factory = {
                        (leftChannelListLayout.parent as? android.view.ViewGroup)?.removeView(leftChannelListLayout)
                        leftChannelListLayout
                    }
                )
            }

            if (rightSettingLayout != null) {
                AndroidView(
                    modifier = Modifier.fillMaxHeight(),
                    factory = {
                        (rightSettingLayout.parent as? android.view.ViewGroup)?.removeView(rightSettingLayout)
                        rightSettingLayout
                    }
                )
            }

            if (statusInfoView != null) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = {
                        (statusInfoView.parent as? android.view.ViewGroup)?.removeView(statusInfoView)
                        statusInfoView
                    }
                )
            }

            if (loadingView != null) {
                AndroidView(
                    modifier = Modifier.wrapContentSize(),
                    factory = {
                        (loadingView.parent as? android.view.ViewGroup)?.removeView(loadingView)
                        loadingView
                    }
                )
            }

            if (rightTopHuikanView != null) {
                AndroidView(
                    modifier = Modifier.wrapContentSize(),
                    factory = {
                        (rightTopHuikanView.parent as? android.view.ViewGroup)?.removeView(rightTopHuikanView)
                        rightTopHuikanView
                    }
                )
            }

            if (backControllerView != null) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = {
                        (backControllerView.parent as? android.view.ViewGroup)?.removeView(backControllerView)
                        backControllerView
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun LivePlayScreenPreview() {
    LivePlayScreen()
}
