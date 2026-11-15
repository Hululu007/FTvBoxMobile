package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutDialogContent(
    modifier: Modifier = Modifier
) {
    val text = "FBox Android 手机版\n基于 TVBoxOS 20260914-1520 改编。\n\n" +
            "本软件提供聚合展示与播放功能，不提供内容源。\n\n" +
            "开源许可：GNU AGPL v3\n" +
            "项目源码：https://github.com/qiangzengli/FTvBoxMobile\n" +
            "上游：https://github.com/q215613905/TVBoxOS\n" +
            "原始项目：https://github.com/CatVodTVOfficial/TVBoxOSC"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = text,
            color = Color(0xFFFFFFFF),
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun AboutDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        AboutDialogContent()
    }
}
