package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingStateView(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF02F8E1)
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(28.dp),
            color = color,
            strokeWidth = 3.dp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF10131B)
@Composable
fun PreviewLoadingStateView() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        LoadingStateView()
    }
}
