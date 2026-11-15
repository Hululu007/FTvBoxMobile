package com.github.tvbox.osc.ui.compose

import android.view.View
import android.widget.ImageButton
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
fun FastSearchScreen(
    backBtn: ImageButton? = null,
    searchTitleView: TextView? = null,
    fenciRecyclerView: TvRecyclerView? = null,
    wordRecyclerView: TvRecyclerView? = null,
    resultRecyclerView: TvRecyclerView? = null,
    filterRecyclerView: TvRecyclerView? = null,
    onSetupBack: ((ImageButton) -> Unit)? = null,
    onSetupTitle: ((TextView) -> Unit)? = null,
    onSetupFenci: ((TvRecyclerView) -> Unit)? = null,
    onSetupWord: ((TvRecyclerView) -> Unit)? = null,
    onSetupResult: ((TvRecyclerView) -> Unit)? = null,
    onSetupFilter: ((TvRecyclerView) -> Unit)? = null
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFAEC6FF),
            background = Color(0xFF10131A),
            surface = Color(0xFF171C26),
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF10131A))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                if (backBtn != null) {
                    AndroidView(
                        modifier = Modifier.size(48.dp),
                        factory = {
                            (backBtn.parent as? android.view.ViewGroup)?.removeView(backBtn)
                            backBtn
                        }
                    )
                } else {
                    AndroidView(
                        modifier = Modifier.size(48.dp),
                        factory = { ctx ->
                            ImageButton(ctx).apply {
                                id = R.id.mobileSearchBack
                                setImageResource(R.drawable.mobile_back)
                                setBackgroundResource(android.R.drawable.btn_default)
                                onSetupBack?.invoke(this)
                            }
                        }
                    )
                }

                if (searchTitleView != null) {
                    AndroidView(
                        modifier = Modifier
                            .width(112.dp)
                            .fillMaxHeight()
                            .padding(end = 8.dp),
                        factory = {
                            (searchTitleView.parent as? android.view.ViewGroup)?.removeView(searchTitleView)
                            searchTitleView
                        }
                    )
                } else {
                    AndroidView(
                        modifier = Modifier
                            .width(112.dp)
                            .fillMaxHeight()
                            .padding(end = 8.dp),
                        factory = { ctx ->
                            TextView(ctx).apply {
                                id = R.id.mSearchTitle
                                setBackgroundResource(R.drawable.mobile_surface)
                                gravity = android.view.Gravity.CENTER
                                setTextColor(android.graphics.Color.WHITE)
                                textSize = 14f
                                maxLines = 2
                                text = "准备搜索\n结果 0"
                                onSetupTitle?.invoke(this)
                            }
                        }
                    )
                }

                if (fenciRecyclerView != null) {
                    AndroidView(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        factory = {
                            (fenciRecyclerView.parent as? android.view.ViewGroup)?.removeView(fenciRecyclerView)
                            fenciRecyclerView
                        }
                    )
                } else {
                    AndroidView(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        factory = { ctx ->
                            TvRecyclerView(ctx).apply {
                                id = R.id.mGridViewWordFenci
                                clipChildren = true
                                clipToPadding = false
                                onSetupFenci?.invoke(this)
                            }
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(start = 8.dp)
            ) {
                if (wordRecyclerView != null) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = {
                            (wordRecyclerView.parent as? android.view.ViewGroup)?.removeView(wordRecyclerView)
                            wordRecyclerView
                        }
                    )
                } else {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            TvRecyclerView(ctx).apply {
                                id = R.id.mGridViewWord
                                clipChildren = true
                                clipToPadding = false
                                onSetupWord?.invoke(this)
                            }
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                if (resultRecyclerView != null) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = {
                            (resultRecyclerView.parent as? android.view.ViewGroup)?.removeView(resultRecyclerView)
                            resultRecyclerView
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
                                visibility = View.INVISIBLE
                                onSetupResult?.invoke(this)
                            }
                        }
                    )
                }

                if (filterRecyclerView != null) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = {
                            (filterRecyclerView.parent as? android.view.ViewGroup)?.removeView(filterRecyclerView)
                            filterRecyclerView
                        }
                    )
                } else {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            TvRecyclerView(ctx).apply {
                                id = R.id.mGridViewFilter
                                clipChildren = true
                                clipToPadding = false
                                visibility = View.INVISIBLE
                                onSetupFilter?.invoke(this)
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
fun FastSearchScreenPreview() {
    FastSearchScreen()
}
