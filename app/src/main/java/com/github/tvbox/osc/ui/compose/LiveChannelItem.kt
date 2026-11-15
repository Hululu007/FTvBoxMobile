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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable for item_live_channel.xml
 * Displays channel number and channel name with selected/focused state highlighting.
 */
@Composable
fun LiveChannelItem(
    channelNum: String,
    channelName: String,
    isSelected: Boolean = false,
    isFocused: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    val bgColor = if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232)
    val strokeColor = if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF)
    val textColor = if (isSelected && !isFocused) Color(0xFF1890FF) else Color.White

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp, vertical = 2.dp)
            .heightIn(min = 48.dp)
            .clip(shape)
            .background(bgColor, shape)
            .border(1.dp, strokeColor, shape)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = channelNum,
            color = textColor,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .width(30.dp)
                .height(29.dp)
                .wrapContentSize(Alignment.Center)
        )
        Text(
            text = channelName,
            color = textColor,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .height(29.dp)
                .wrapContentSize(Alignment.CenterStart)
                .padding(end = 4.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun LiveChannelItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            LiveChannelItem(channelNum = "1", channelName = "CCTV-1 综合", isSelected = false)
            LiveChannelItem(channelNum = "2", channelName = "CCTV-2 财经", isSelected = true)
        }
    }
}
