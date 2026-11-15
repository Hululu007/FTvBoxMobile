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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable for item_search_danmu_result.xml
 * FrameLayout (mobile_surface) containing a mobile_choice row with danmu name.
 */
@Composable
fun SearchDanmuResultItem(
    name: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val outerShape = RoundedCornerShape(10.dp)
    val innerShape = RoundedCornerShape(10.dp)

    val innerBgColor = if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232)
    val innerStrokeColor = if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(outerShape)
            .background(Color(0xFF1A2232), outerShape)
            .border(1.dp, Color(0x22FFFFFF), outerShape)
            .padding(bottom = 5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .clip(innerShape)
                .background(innerBgColor, innerShape)
                .border(1.dp, innerStrokeColor, innerShape)
                .clickable(onClick = onClick)
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = name,
                color = Color(0xFFFFFFFF),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun SearchDanmuResultItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SearchDanmuResultItem(name = "第一弹弹幕集 - 第1集", isSelected = false)
            SearchDanmuResultItem(name = "第一弹弹幕集 - 第2集", isSelected = true)
        }
    }
}
