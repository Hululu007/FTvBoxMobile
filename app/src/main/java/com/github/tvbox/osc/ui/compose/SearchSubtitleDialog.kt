package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tvbox.osc.bean.Subtitle

private val ShapeRadius = RoundedCornerShape(10.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSubtitleDialogContent(
    searchWord: String,
    onSearchWordChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    subtitles: List<Subtitle>,
    isLoading: Boolean,
    onSubtitleClick: (Subtitle) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchWord,
                onValueChange = onSearchWordChange,
                placeholder = { Text("请输入字幕名称", color = Color(0x80FFFFFF), fontSize = 15.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch(searchWord.trim()) }),
                shape = ShapeRadius,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF131822),
                    unfocusedContainerColor = Color(0xFF131822),
                    focusedBorderColor = Color(0xFF00C2FF),
                    unfocusedBorderColor = Color(0x22FFFFFF)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            Row(
                modifier = Modifier
                    .width(80.dp)
                    .height(56.dp)
                    .clip(ShapeRadius)
                    .background(Color(0xFF1A2232), ShapeRadius)
                    .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                    .clickable { onSearch(searchWord.trim()) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("搜索", color = Color.White, fontSize = 14.sp)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .heightIn(min = 120.dp, max = 280.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    color = Color(0xFF02F8E1),
                    strokeWidth = 2.dp
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(subtitles) { item ->
                        SearchSubtitleResultItem(
                            name = item.getName() ?: "",
                            info = if (item.getIsZip()) "压缩包" else "",
                            onClick = { onSubtitleClick(item) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun SearchSubtitleDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        SearchSubtitleDialogContent(
            searchWord = "测试",
            onSearchWordChange = {},
            onSearch = {},
            subtitles = emptyList(),
            isLoading = false,
            onSubtitleClick = {}
        )
    }
}
