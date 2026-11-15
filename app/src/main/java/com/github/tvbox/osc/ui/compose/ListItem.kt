package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ListItem(
    name: String,
    note: String = "",
    pic: String? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AppListItem(
        name = name,
        note = note,
        pic = pic,
        onClick = onClick,
        modifier = modifier
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun ListItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(modifier = Modifier.padding(8.dp)) {
            ListItem(name = "电影大片列表项", note = "4K超清", pic = null)
        }
    }
}
