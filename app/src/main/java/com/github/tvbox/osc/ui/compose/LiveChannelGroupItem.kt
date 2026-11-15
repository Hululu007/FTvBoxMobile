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
 * Composable for item_live_channel_group.xml
 * Used by LiveChannelGroupAdapter and LiveEpgDateAdapter.
 */
@Composable
fun LiveChannelGroupItem(
    groupName: String,
    isSelected: Boolean = false,
    isFocused: Boolean = false,
    isEpgDateMode: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    val bgColor = if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232)
    val strokeColor = if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF)

    val textColor = if (isEpgDateMode) {
        if (isSelected && !isFocused) Color(0xFF1890FF) else Color(0xBBFFFFFF)
    } else {
        if (isSelected && !isFocused) Color(0xFF1890FF) else Color.White
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp, vertical = 2.dp)
            .heightIn(min = 48.dp)
            .clip(shape)
            .background(bgColor, shape)
            .border(1.dp, strokeColor, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = groupName,
            color = textColor,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .height(29.dp)
                .wrapContentSize(Alignment.Center)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun LiveChannelGroupItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            LiveChannelGroupItem(groupName = "央视频道", isSelected = false)
            LiveChannelGroupItem(groupName = "卫视频道", isSelected = true)
        }
    }
}
