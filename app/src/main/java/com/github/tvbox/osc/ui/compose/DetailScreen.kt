package com.github.tvbox.osc.ui.compose

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
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
fun DetailScreen(
    topLayout: View? = null,
    emptyPlaylist: View? = null,
    flagGrid: TvRecyclerView? = null,
    seriesGroupTv: View? = null,
    qualityGrid: TvRecyclerView? = null,
    mainGrid: TvRecyclerView? = null,
    previewPlayer: View? = null,
    previewPlayerBlock: View? = null,
    onSetupMainGrid: ((TvRecyclerView) -> Unit)? = null
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
            Column(modifier = Modifier.fillMaxSize()) {
                if (topLayout != null) {
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = {
                            (topLayout.parent as? android.view.ViewGroup)?.removeView(topLayout)
                            topLayout
                        }
                    )
                }

                if (emptyPlaylist != null) {
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = {
                            (emptyPlaylist.parent as? android.view.ViewGroup)?.removeView(emptyPlaylist)
                            emptyPlaylist
                        }
                    )
                }

                if (flagGrid != null) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .padding(horizontal = 16.dp),
                        factory = {
                            (flagGrid.parent as? android.view.ViewGroup)?.removeView(flagGrid)
                            flagGrid
                        }
                    )
                }

                if (seriesGroupTv != null) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        factory = {
                            (seriesGroupTv.parent as? android.view.ViewGroup)?.removeView(seriesGroupTv)
                            seriesGroupTv
                        }
                    )
                }

                if (qualityGrid != null) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .padding(horizontal = 16.dp),
                        factory = {
                            (qualityGrid.parent as? android.view.ViewGroup)?.removeView(qualityGrid)
                            qualityGrid
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    if (mainGrid != null) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = {
                                (mainGrid.parent as? android.view.ViewGroup)?.removeView(mainGrid)
                                mainGrid
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
                                    onSetupMainGrid?.invoke(this)
                                }
                            }
                        )
                    }
                }
            }

            if (previewPlayer != null) {
                AndroidView(
                    modifier = Modifier
                        .width(235.dp)
                        .height(150.dp)
                        .padding(start = 25.dp, top = 10.dp),
                    factory = {
                        (previewPlayer.parent as? android.view.ViewGroup)?.removeView(previewPlayer)
                        previewPlayer
                    }
                )
            }

            if (previewPlayerBlock != null) {
                AndroidView(
                    modifier = Modifier
                        .width(235.dp)
                        .height(150.dp)
                        .padding(start = 25.dp, top = 10.dp),
                    factory = {
                        (previewPlayerBlock.parent as? android.view.ViewGroup)?.removeView(previewPlayerBlock)
                        previewPlayerBlock
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    DetailScreen()
}
