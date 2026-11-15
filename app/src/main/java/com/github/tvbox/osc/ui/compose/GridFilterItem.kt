package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GridFilterItem(
    filterName: String,
    filterValues: List<String>,
    selectedValue: String? = null,
    onValueSelect: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = filterName,
            color = Color(0xFFFFFFFF),
            fontSize = 15.sp,
            modifier = Modifier
                .padding(end = 5.dp, bottom = 8.dp)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            filterValues.forEach { value ->
                GridFilterValueItem(
                    text = value,
                    isSelected = value == selectedValue,
                    onClick = { onValueSelect(value) },
                    modifier = Modifier.widthIn(min = 80.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun GridFilterItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(modifier = Modifier.padding(16.dp)) {
            GridFilterItem(
                filterName = "类型",
                filterValues = listOf("全部", "动作", "喜剧", "爱情", "科幻", "悬疑"),
                selectedValue = "动作"
            )
        }
    }
}
