package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// mobile_surface: bg #1A2232, 10dp radius, stroke #22FFFFFF
// color_FFFFFF = #ffffffff
private val ShapeRadius = RoundedCornerShape(10.dp)
private val SurfaceBg = Color(0xFF1A2232)
private val SurfaceStroke = Color(0x22FFFFFF)
private val TextColor = Color(0xFFFFFFFF)

@Composable
fun SearchWordHotItem(
    word: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(ShapeRadius)
            .background(SurfaceBg, ShapeRadius)
            .border(1.dp, SurfaceStroke, ShapeRadius)
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Text(
            text = word,
            color = TextColor,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun SearchWordHotItemPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        SearchWordHotItem(word = "热门搜索词示例", onClick = {})
        SearchWordHotItem(word = "另一个搜索词", onClick = {})
    }
}
