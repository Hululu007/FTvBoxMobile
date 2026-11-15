package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.tvbox.osc.R
import com.owen.tvrecyclerview.widget.TvRecyclerView

@Composable
fun GridScreen(
    recyclerView: TvRecyclerView? = null,
    onSetupRecyclerView: ((TvRecyclerView) -> Unit)? = null
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
                .padding(horizontal = 16.dp, vertical = 5.dp)
        ) {
            if (recyclerView != null) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = {
                        (recyclerView.parent as? android.view.ViewGroup)?.removeView(recyclerView)
                        recyclerView
                    }
                )
            } else {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        TvRecyclerView(ctx).apply {
                            id = R.id.mGridView
                            clipChildren = true
                            clipToPadding = false
                            onSetupRecyclerView?.invoke(this)
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun GridScreenPreview() {
    GridScreen()
}
