package com.github.tvbox.osc.ui.compose

import android.view.View
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.tvbox.osc.R
import com.github.tvbox.osc.ui.tv.widget.NoScrollViewPager
import com.owen.tvrecyclerview.widget.TvRecyclerView

@Composable
fun HomeScreen(
    appName: String = "FBox",
    dateText: String = "",
    showDate: Boolean = false,
    onTitleClick: () -> Unit = {},
    menuRecyclerView: TvRecyclerView? = null,
    viewPager: View? = null,
    titleView: TextView? = null,
    dateView: TextView? = null,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF10131A))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (titleView != null) {
                    AndroidView(
                        modifier = Modifier.weight(1f),
                        factory = {
                            (titleView.parent as? android.view.ViewGroup)?.removeView(titleView)
                            titleView
                        }
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onTitleClick() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = appName,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.mobile_expand),
                            contentDescription = "Expand",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                if (showDate || dateView != null) {
                    if (dateView != null) {
                        AndroidView(
                            factory = {
                                (dateView.parent as? android.view.ViewGroup)?.removeView(dateView)
                                dateView
                            }
                        )
                    } else {
                        Text(
                            text = dateText,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            ) {
                if (menuRecyclerView != null) {
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = {
                            (menuRecyclerView.parent as? android.view.ViewGroup)?.removeView(menuRecyclerView)
                            menuRecyclerView
                        }
                    )
                } else {
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
fun HomeScreenPreview() {
    HomeScreen()
}
