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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun GridFilterValueItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232)
    val strokeColor = if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF)
    val textColor = if (isSelected) Color(0xFF02F8E1) else Color.White
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(2.dp)
            .clip(ShapeRadius)
            .background(bgColor, ShapeRadius)
            .border(1.dp, strokeColor, ShapeRadius)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = fontWeight,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun GridFilterValueItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(modifier = Modifier.padding(8.dp)) {
            GridFilterValueItem(text = "全部", isSelected = true)
            Spacer(modifier = Modifier.height(4.dp))
            GridFilterValueItem(text = "动作片", isSelected = false)
        }
    }
}
