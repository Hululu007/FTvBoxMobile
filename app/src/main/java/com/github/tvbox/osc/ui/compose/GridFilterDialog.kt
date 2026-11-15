package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tvbox.osc.bean.MovieSort

private val ShapeRadius = RoundedCornerShape(10.dp)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GridFilterDialogContent(
    sortData: MovieSort.SortData,
    onFilterSelected: (key: String, value: String, deselect: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = sortData.filters ?: emptyList()
    val filterSelect = sortData.filterSelect

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .heightIn(max = 400.dp)
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        filters.forEach { filter ->
            val key = filter.key ?: return@forEach
            val filterValues = filter.values ?: emptyMap()
            val selectedVal = filterSelect?.get(key)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Text(
                    text = filter.name ?: "",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    filterValues.forEach { (label, value) ->
                        val isSelected = value == selectedVal
                        val bgColor = if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232)
                        val strokeColor = if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF)
                        val textColor = if (isSelected) Color(0xFFAEC6FF) else Color.White

                        Box(
                            modifier = Modifier
                                .clip(ShapeRadius)
                                .background(bgColor, ShapeRadius)
                                .border(1.dp, strokeColor, ShapeRadius)
                                .clickable {
                                    val deselect = isSelected
                                    onFilterSelected(key, value, deselect)
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = textColor,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun GridFilterDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        GridFilterDialogContent(
            sortData = MovieSort.SortData(),
            onFilterSelected = { _, _, _ -> }
        )
    }
}
