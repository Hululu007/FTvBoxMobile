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

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun LocalFileItem(
    typeText: String,
    name: String,
    info: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(bottom = 4.dp)
            .clip(ShapeRadius)
            .background(Color(0xFF1A2232), ShapeRadius)
            .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp)
    ) {
        Text(
            text = typeText,
            color = Color.White,
            fontSize = 14.sp,
            maxLines = 1,
            modifier = Modifier.width(44.dp)
        )
        Text(
            text = name,
            color = Color.White,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = info,
            color = Color(0xBBFFFFFF),
            fontSize = 12.sp,
            maxLines = 1,
            textAlign = TextAlign.End,
            modifier = Modifier.width(121.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun LocalFileItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(modifier = Modifier.padding(8.dp)) {
            LocalFileItem(typeText = "目录", name = "Downloads", info = "进入")
            LocalFileItem(typeText = "文件", name = "movie.mp4", info = "1.5 GB")
        }
    }
}
