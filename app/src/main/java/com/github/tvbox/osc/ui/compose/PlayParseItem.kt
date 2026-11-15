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
private val ShapeRadius = RoundedCornerShape(10.dp)
private val SurfaceBg = Color(0xFF1A2232)
private val SurfaceStroke = Color(0x22FFFFFF)
private val CyanAccent = Color(0xFF02F8E1)

@Composable
fun PlayParseItem(
    name: String,
    isDefault: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .clip(ShapeRadius)
            .background(SurfaceBg, ShapeRadius)
            .border(1.dp, SurfaceStroke, ShapeRadius)
            .clickable(onClick = onClick)
            .padding(5.dp)
    ) {
        Text(
            text = name,
            color = if (isDefault) CyanAccent else Color.White,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun PlayParseItemPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        PlayParseItem(name = "解析线路1", isDefault = true, onClick = {})
        PlayParseItem(name = "解析线路2", isDefault = false, onClick = {})
    }
}
