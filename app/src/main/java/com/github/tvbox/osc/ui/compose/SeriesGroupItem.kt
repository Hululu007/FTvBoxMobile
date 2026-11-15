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
 * Composable for item_series_group.xml
 * Episode grouping button (e.g. 1-50, 51-100) in drama detail.
 */
@Composable
fun SeriesGroupItem(
    title: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    val bgColor = if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232)
    val strokeColor = if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF)
    val textColor = if (isSelected) Color(0xFF02F8E1) else Color.White

    Box(
        modifier = modifier
            .wrapContentWidth()
            .heightIn(min = 48.dp)
            .padding(end = 5.dp)
            .clip(shape)
            .background(bgColor, shape)
            .border(1.dp, strokeColor, shape)
            .clickable(onClick = onClick)
            .padding(start = 12.dp, top = 2.dp, end = 12.dp, bottom = 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun SeriesGroupItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SeriesGroupItem(title = "1-50", isSelected = true)
            SeriesGroupItem(title = "51-100", isSelected = false)
        }
    }
}
