package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable for item_live.xml
 * Displays either live channel/group name or "+新增直播" button.
 */
@Composable
fun LiveItem(
    title: String,
    isAddLive: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .wrapContentSize()
            .heightIn(min = 48.dp)
            .clip(shape)
            .background(Color(0xFF1A2232), shape)
            .border(1.dp, Color(0x22FFFFFF), shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isAddLive) {
            Text(
                text = "+新增直播",
                color = Color(0xFFCBF46A),
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .width(105.dp)
                    .height(28.dp)
                    .wrapContentSize(Alignment.Center)
                    .padding(horizontal = 2.dp)
            )
        } else {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .width(105.dp)
                    .height(28.dp)
                    .wrapContentSize(Alignment.Center)
                    .padding(horizontal = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun LiveItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LiveItem(title = "CCTV-1")
            LiveItem(title = "", isAddLive = true)
        }
    }
}
