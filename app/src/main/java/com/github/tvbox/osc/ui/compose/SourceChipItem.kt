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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ShapeRadius = RoundedCornerShape(10.dp)

// Text color from XML: #E6EAF2
private val ChipTextColor = Color(0xFFE6EAF2)

@Composable
fun SourceChipItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232)
    val strokeColor = if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF)
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(48.dp)
            .defaultMinSize(minWidth = 64.dp)
            .widthIn(max = 200.dp)
            .clip(ShapeRadius)
            .background(bgColor, ShapeRadius)
            .border(1.dp, strokeColor, ShapeRadius)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = text,
            color = ChipTextColor,
            fontSize = 14.sp,
            fontWeight = fontWeight,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun SourceChipItemPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SourceChipItem(text = "全部", isSelected = true, onClick = {})
        SourceChipItem(text = "CCTV1", isSelected = false, onClick = {})
        SourceChipItem(text = "长文字频道名称", isSelected = false, onClick = {})
    }
}
