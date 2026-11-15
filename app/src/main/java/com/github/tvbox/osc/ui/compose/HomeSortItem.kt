package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tvbox.osc.R

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun HomeSortItem(
    title: String,
    isSelected: Boolean,
    hasFilters: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232)
    val strokeColor = if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF)
    val textColor = if (isSelected) Color(0xFF02F8E1) else Color(0xBBFFFFFF)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .clip(ShapeRadius)
            .background(bgColor, ShapeRadius)
            .border(1.dp, strokeColor, ShapeRadius)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp)
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = 19.sp
        )
        if (isSelected && hasFilters) {
            Spacer(modifier = Modifier.width(2.dp))
            Image(
                painter = painterResource(id = R.drawable.icon_filter),
                contentDescription = "Filter",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun HomeSortItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)) {
            HomeSortItem(title = "电影", isSelected = true, hasFilters = true)
            HomeSortItem(title = "电视剧", isSelected = false, hasFilters = false)
        }
    }
}
