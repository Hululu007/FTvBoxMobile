package com.github.tvbox.osc.ui.compose

import com.fbox.mobile.MobileIconAction
import com.github.tvbox.osc.R

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        MobileIconAction(
            R.drawable.ic_action_download, buttonText, onDownloadClick,
            modifier = Modifier.padding(top = 16.dp), enabled = enabled, caption = buttonText
        )
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
