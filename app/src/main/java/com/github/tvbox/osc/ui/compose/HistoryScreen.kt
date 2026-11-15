package com.github.tvbox.osc.ui.compose

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
fun HistoryScreen(
    title: String = "历史记录",
    delTipVisible: Boolean = false,
    delTipText: String = "确定键删除当前选中记录",
    showTopBar: Boolean = false,
    onDeleteClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF10131A))
        ) {
            if (showTopBar) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (delTipVisible) {
                        Text(
                            text = delTipText,
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1A2232))
                            .clickable { onDeleteClick() }
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_delete),
                            contentDescription = "Delete"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1A2232))
                            .clickable { onClearClick() }
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_clear),
                            contentDescription = "Clear"
                        )
                    }
                }
            } else if (delTipVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = delTipText,
                        color = Color(0xFFAEC6FF),
                        fontSize = 13.sp
                    )
                }
            }

            if (recyclerView != null) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    factory = {
                        (recyclerView.parent as? android.view.ViewGroup)?.removeView(recyclerView)
                        recyclerView
                    }
                )
            } else {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
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
fun HistoryScreenPreview() {
    HistoryScreen(showTopBar = true, delTipVisible = true)
}
