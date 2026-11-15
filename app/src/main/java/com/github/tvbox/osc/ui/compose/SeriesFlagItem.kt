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
// shape_source_flag_line: solid #1890FF, 2dp tall, corners 2dp
private val ShapeRadius = RoundedCornerShape(10.dp)
private val SurfaceBg = Color(0xFF1A2232)
private val SurfaceStroke = Color(0x22FFFFFF)
private val FlagLineColor = Color(0xFF1890FF)

@Composable
fun SeriesFlagItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .wrapContentWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(end = 5.dp)
            .clip(ShapeRadius)
            .background(SurfaceBg, ShapeRadius)
            .border(1.dp, SurfaceStroke, ShapeRadius)
            .clickable(onClick = onClick)
            .padding(start = 12.dp, top = 2.dp, end = 12.dp, bottom = 0.dp)
    ) {
        Text(
            text = name,
            color = Color.White,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        // Selection indicator line — matches shape_source_flag_line
        if (isSelected) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
                    .height(2.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(FlagLineColor)
            )
        } else {
            Spacer(modifier = Modifier.height(4.dp)) // balance height when not selected
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun SeriesFlagItemPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        SeriesFlagItem(name = "第一季", isSelected = true, onClick = {})
        SeriesFlagItem(name = "第二季", isSelected = false, onClick = {})
        SeriesFlagItem(name = "第三季", isSelected = false, onClick = {})
    }
}
