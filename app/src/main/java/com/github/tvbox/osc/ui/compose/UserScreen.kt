package com.github.tvbox.osc.ui.compose

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.tvbox.osc.R
import com.owen.tvrecyclerview.widget.TvRecyclerView

@Composable
fun UserScreen(
    onHistoryClick: () -> Unit = {},
    onLiveClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    onLocalClick: () -> Unit = {},
    onCollectClick: () -> Unit = {},
    onPushClick: () -> Unit = {},
    hotListView: TvRecyclerView? = null,
    onSetupHotList: ((TvRecyclerView) -> Unit)? = null
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UserNavButton(
                    modifier = Modifier.weight(1f),
                    title = "历史",
                    iconRes = R.drawable.icon_history,
                    onClick = onHistoryClick
                )
                UserNavButton(
                    modifier = Modifier.weight(1f),
                    title = "本地",
                    iconRes = R.drawable.ic_mobile_folder,
                    onClick = onLocalClick
                )
                UserNavButton(
                    modifier = Modifier.weight(1f),
                    title = "收藏",
                    iconRes = R.drawable.icon_collect,
                    onClick = onCollectClick
                )
                UserNavButton(
                    modifier = Modifier.weight(1f),
                    title = "链接",
                    iconRes = R.drawable.icon_push,
                    onClick = onPushClick
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (hotListView != null) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = {
                            (hotListView.parent as? android.view.ViewGroup)?.removeView(hotListView)
                            hotListView
                        }
                    )
                } else {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            TvRecyclerView(ctx).apply {
                                id = R.id.tvHotList
                                clipChildren = true
                                clipToPadding = false
                                onSetupHotList?.invoke(this)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun UserNavButton(
    modifier: Modifier = Modifier,
    title: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF1A2232))
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = title,
            color = Color(0xCCFFFFFF),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview
@Composable
fun UserScreenPreview() {
    UserScreen()
}
