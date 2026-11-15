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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tvbox.osc.bean.VodInfo

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun EpisodeDialogContent(
    episodes: List<VodInfo.VodSeries>,
    selectedIndex: Int,
    onEpisodeSelected: (Int) -> Unit,
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
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 360.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 72.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(episodes) { index, item ->
                    val isSelected = index == selectedIndex
                    val bgColor = if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232)
                    val strokeColor = if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF)
                    val textColor = if (isSelected) Color(0xFF02F8E1) else Color.White

                    Box(
                        modifier = Modifier
                            .clip(ShapeRadius)
                            .background(bgColor, ShapeRadius)
                            .border(1.dp, strokeColor, ShapeRadius)
                            .clickable { onEpisodeSelected(index) }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.name ?: "",
                            color = textColor,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun EpisodeDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        EpisodeDialogContent(
            episodes = emptyList(),
            selectedIndex = 0,
            onEpisodeSelected = {},
            title = "选集"
        )
    }
}
