package com.github.tvbox.osc.ui.compose

import android.view.View
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
import androidx.compose.ui.viewinterop.AndroidView
import com.github.tvbox.osc.ui.tv.widget.AudioWaveView

/**
 * Composable for epglist_item.xml
 * Used in LiveEpgAdapter and MyEpgAdapter.
 */
@Composable
fun EpgListItem(
    title: String,
    timeText: String,
    isSelected: Boolean = false,
    isFocused: Boolean = false,
    isHighlightedColor: Boolean = false,
    shiyiText: String? = null,
    shiyiBgColor: Color = Color.Transparent,
    shiyiTextColor: Color = Color.White,
    showAudioWave: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    val bgColor = if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232)
    val strokeColor = if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF)
    val contentTextColor = if (isHighlightedColor) Color(0xFF1890FF) else Color.White

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(shape)
            .background(bgColor, shape)
            .border(1.dp, strokeColor, shape)
            .clickable(onClick = onClick)
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(2.dp)
        ) {
            Text(
                text = timeText,
                color = contentTextColor,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = title,
                color = contentTextColor,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(
            modifier = Modifier
                .width(55.dp)
                .padding(horizontal = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (shiyiText != null) {
                Box(
                    modifier = Modifier
                        .size(width = 44.dp, height = 14.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(shiyiBgColor)
                        .padding(horizontal = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = shiyiText,
                        color = shiyiTextColor,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }
            if (showAudioWave) {
                AndroidView(
                    factory = { context ->
                        AudioWaveView(context)
                    },
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .size(width = 44.dp, height = 14.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun EpgListItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            EpgListItem(
                title = "新闻联播",
                timeText = "19:00--19:30",
                isSelected = true,
                isHighlightedColor = true,
                shiyiText = "直播中",
                shiyiBgColor = Color.Yellow,
                shiyiTextColor = Color.Red,
                showAudioWave = true
            )
            EpgListItem(
                title = "焦点访谈",
                timeText = "19:38--20:00",
                isSelected = false,
                shiyiText = "回看",
                shiyiBgColor = Color.Blue,
                shiyiTextColor = Color.White,
                showAudioWave = false
            )
        }
    }
}
