package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tvbox.osc.R

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun SelectDialogItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232)
    val strokeColor = if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF)
    // selected text: 0xffaec6ff (from -0x513901 = 0xffaec6ff)
    val textColor = if (isSelected) Color(0xFFAEC6FF) else Color.White
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(vertical = 2.dp)
            .clip(ShapeRadius)
            .background(bgColor, ShapeRadius)
            .border(1.dp, strokeColor, ShapeRadius)
            .clickable(onClick = onClick)
            .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 15.sp,
            fontWeight = fontWeight,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Icon(
                painter = painterResource(id = R.drawable.ic_mobile_check),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun SelectDialogItemPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        SelectDialogItem(text = "1080P 高清", isSelected = true, onClick = {})
        SelectDialogItem(text = "720P 标清", isSelected = false, onClick = {})
    }
}
