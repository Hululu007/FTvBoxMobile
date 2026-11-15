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
fun ModelScreen(
    scrollView: View? = null
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFAEC6FF),
            background = Color(0xFF10131A),
            surface = Color(0xFF171C26),
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF10131A))
        ) {
            if (scrollView != null) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = {
                        (scrollView.parent as? android.view.ViewGroup)?.removeView(scrollView)
                        scrollView
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun ModelScreenPreview() {
    ModelScreen()
}
