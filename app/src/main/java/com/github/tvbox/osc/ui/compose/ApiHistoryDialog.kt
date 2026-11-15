package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun ApiHistoryDialogContent(
    items: List<String>,
    selectedIndex: Int,
    onItemClick: (String, Int) -> Unit,
    onDeleteClick: (String, Int) -> Unit,
    title: String = "配置历史",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                itemsIndexed(items) { index, item ->
                    val isSelected = index == selectedIndex
                    val bgColor = if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232)
                    val strokeColor = if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF)
                    val textColor = if (isSelected) Color(0xFFAEC6FF) else Color.White

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(ShapeRadius)
                            .background(bgColor, ShapeRadius)
                            .border(1.dp, strokeColor, ShapeRadius)
                            .selectable(selected = isSelected, onClick = { onItemClick(item, index) })
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item,
                            color = textColor,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(1f)
                        )
                        Text(
                            text = "删除",
                            color = Color(0xFFFFB4AB),
                            fontSize = 13.sp,
                            modifier = Modifier
                                .semantics { contentDescription = "删除配置 $item" }
                                .clickable { onDeleteClick(item, index) }
                                .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun ApiHistoryDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        ApiHistoryDialogContent(
            items = listOf("http://api1.com", "http://api2.com"),
            selectedIndex = 0,
            onItemClick = { _, _ -> },
            onDeleteClick = { _, _ -> }
        )
    }
}
