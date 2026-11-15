package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tvbox.osc.R

@Composable
fun EmptyStateView(
    modifier: Modifier = Modifier,
    tipText: String = "没找到数据"
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_empty),
                contentDescription = null,
                modifier = Modifier.size(66.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = tipText,
                color = Color.White,
                fontSize = 15.sp
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF10131B)
@Composable
fun PreviewEmptyStateView() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        EmptyStateView()
    }
}
