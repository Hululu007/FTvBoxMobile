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

// mobile_choice selector
// default  → mobile_surface: bg #1A2232, 10dp radius, stroke #22FFFFFF
// selected → mobile_accent:  bg #1E3860, 10dp radius, stroke #5900C2FF
// color_CCFFFFFF = #BBFFFFFF (as defined in colors.xml)
private val ShapeRadius = RoundedCornerShape(10.dp)
private val TextColor = Color(0xBBFFFFFF) // color_CCFFFFFF

@Composable
fun SettingMenuItem(
    name: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232)
    val strokeColor = if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(bottom = 5.dp)
            .clip(ShapeRadius)
            .background(bgColor, ShapeRadius)
            .border(1.dp, strokeColor, ShapeRadius)
            .clickable(onClick = onClick)
            .padding(2.dp)
    ) {
        Text(
            text = name,
            color = TextColor,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun SettingMenuItemPreview() {
    Column {
        SettingMenuItem(name = "画质设置", isSelected = true, onClick = {})
        SettingMenuItem(name = "字幕设置", isSelected = false, onClick = {})
        SettingMenuItem(name = "音频设置", isSelected = false, onClick = {})
    }
}
