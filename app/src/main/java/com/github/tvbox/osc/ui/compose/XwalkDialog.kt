package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ButtonShape = RoundedCornerShape(10.dp)

@Composable
fun XwalkDialogContent(
    archInfo: String,
    buttonText: String,
    enabled: Boolean,
    onDownloadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "下载XWalkView运行组件\n$archInfo",
            color = Color(0xFFFFFFFF),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .width(180.dp)
                .height(48.dp)
                .clip(ButtonShape)
                .background(
                    if (enabled) Color(0xFF1E3860) else Color(0xFF1A2232),
                    ButtonShape
                )
                .border(
                    1.dp,
                    if (enabled) Color(0x5900C2FF) else Color(0x22FFFFFF),
                    ButtonShape
                )
                .clickable(enabled = enabled, onClick = onDownloadClick),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buttonText,
                color = if (enabled) Color(0xFFFFFFFF) else Color(0x80FFFFFF),
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun XwalkDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        XwalkDialogContent(
            archInfo = "Arch:arm64-v8a",
            buttonText = "下载",
            enabled = true,
            onDownloadClick = {}
        )
    }
}
