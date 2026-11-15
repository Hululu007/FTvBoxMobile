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
 * Composable for item_search_subtitle_result.xml
 * FrameLayout (mobile_surface) containing a mobile_choice row with subtitle title and info (e.g. 压缩包 / 文件).
 */
@Composable
fun SearchSubtitleResultItem(
    name: String,
    info: String = "",
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .clip(innerShape)
                .background(innerBgColor, innerShape)
                .border(1.dp, innerStrokeColor, innerShape)
                .clickable(onClick = onClick)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                color = Color(0xFFFFFFFF),
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if (info.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = info,
                    color = Color(0xFFFFFFFF),
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun SearchSubtitleResultItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SearchSubtitleResultItem(
                name = "肖申克的救赎.The.Shawshank.Redemption.1994.BluRay.chs&eng.ass",
                info = "压缩包",
                isSelected = false
            )
            SearchSubtitleResultItem(
                name = "肖申克的救赎.The.Shawshank.Redemption.1994.srt",
                info = "文件",
                isSelected = true
            )
        }
    }
}
