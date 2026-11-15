package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tvbox.osc.bean.Movie

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun QuickSearchDialogContent(
    words: List<String>,
    results: List<Movie.Video>,
    onWordClick: (String) -> Unit,
    onVideoClick: (Movie.Video) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Text(
            text = "其他数据源中的相关资源, 没有找到可以尝试选词重搜",
            color = Color.White,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (words.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(words) { word ->
                    Box(
                        modifier = Modifier
                            .clip(ShapeRadius)
                            .background(Color(0xFF1A2232), ShapeRadius)
                            .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                            .clickable { onWordClick(word) }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = word, color = Color.White, fontSize = 13.sp)
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 280.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(results) { video ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(ShapeRadius)
                            .background(Color(0xFF1A2232), ShapeRadius)
                            .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                            .clickable { onVideoClick(video) }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "${video.sourceKey ?: ""} : ${video.name ?: ""}",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun QuickSearchDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        QuickSearchDialogContent(
            words = listOf("词1", "词2"),
            results = emptyList(),
            onWordClick = {},
            onVideoClick = {}
        )
    }
}
