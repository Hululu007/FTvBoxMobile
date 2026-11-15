package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val DEFAULT_KEYBOARD_KEYS: List<String> = listOf(
    "远程搜索", "删除", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
    "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
)

@Composable
fun KeyboardLayout(
    onKeyClick: (pos: Int, key: String) -> Unit,
    modifier: Modifier = Modifier,
    keys: List<String> = DEFAULT_KEYBOARD_KEYS
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(6),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        itemsIndexed(
            items = keys,
            span = { index, _ ->
                if (index == 0 || index == 1) GridItemSpan(3) else GridItemSpan(1)
            }
        ) { index, key ->
            KeyboardItem(
                keyText = key,
                onClick = { onKeyClick(index, key) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF10131B, widthDp = 360)
@Composable
fun PreviewKeyboardLayout() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        KeyboardLayout(onKeyClick = { _, _ -> })
    }
}
