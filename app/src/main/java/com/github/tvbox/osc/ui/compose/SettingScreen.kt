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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.tvbox.osc.R
import com.github.tvbox.osc.ui.tv.widget.NoScrollViewPager
import com.owen.tvrecyclerview.widget.TvRecyclerView

@Composable
fun SettingScreen(
    showMenu: Boolean = false,
    menuRecyclerView: TvRecyclerView? = null,
    viewPager: View? = null,
    onSetupMenu: ((TvRecyclerView) -> Unit)? = null,
    onSetupViewPager: ((NoScrollViewPager) -> Unit)? = null
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFAEC6FF),
            background = Color(0xFF10131A),
            surface = Color(0xFF171C26),
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF10131A))
        ) {
            if (showMenu) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .fillMaxHeight()
                        .background(Color(0x6632364E))
                        .padding(horizontal = 6.dp, vertical = 10.dp)
                ) {
                    if (menuRecyclerView != null) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = {
                                (menuRecyclerView.parent as? android.view.ViewGroup)?.removeView(menuRecyclerView)
                                menuRecyclerView
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
                                    onSetupMenu?.invoke(this)
                                }
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                if (viewPager != null) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = {
                            (viewPager.parent as? android.view.ViewGroup)?.removeView(viewPager)
                            viewPager
                        }
                    )
                } else {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            NoScrollViewPager(ctx).apply {
                                id = R.id.mViewPager
                                onSetupViewPager?.invoke(this)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingScreenPreview() {
    SettingScreen(showMenu = false)
}
