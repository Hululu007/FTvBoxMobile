package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chad.library.R as BRvahR

enum class LoadMoreStatus {
    LOADING,
    FAIL,
    END,
    GONE
}

@Composable
fun LoadMoreItem(
    status: LoadMoreStatus,
    onRetryClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (status == LoadMoreStatus.GONE) return

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        when (status) {
            LoadMoreStatus.LOADING -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = BRvahR.string.brvah_loading),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
            LoadMoreStatus.FAIL -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = onRetryClick)
                ) {
                    Text(
                        text = stringResource(id = BRvahR.string.brvah_load_failed),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
            LoadMoreStatus.END -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(id = BRvahR.string.brvah_load_end),
                        color = Color(0xFFAAAAAA),
                        fontSize = 14.sp
                    )
                }
            }
            LoadMoreStatus.GONE -> {}
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun LoadMoreItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(modifier = Modifier.padding(8.dp)) {
            LoadMoreItem(status = LoadMoreStatus.LOADING)
            LoadMoreItem(status = LoadMoreStatus.FAIL)
            LoadMoreItem(status = LoadMoreStatus.END)
        }
    }
}
