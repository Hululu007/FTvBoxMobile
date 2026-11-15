package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> SelectDialogContent(
    items: List<T>,
    selectedIndex: Int,
    itemLabel: (T) -> String,
    onItemSelected: (T, Int) -> Unit,
    title: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        if (!title.isNullOrEmpty()) {
            Text(
                text = title,
                color = Color(0x80FFFFFF),
                fontSize = 13.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 380.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
            ) {
                itemsIndexed(items) { index, item ->
                    SelectDialogItem(
                        text = itemLabel(item),
                        isSelected = index == selectedIndex,
                        onClick = { onItemSelected(item, index) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun SelectDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        SelectDialogContent(
            items = listOf("选项 1", "选项 2", "选项 3"),
            selectedIndex = 1,
            itemLabel = { it },
            onItemSelected = { _, _ -> },
            title = "请选择"
        )
    }
}
