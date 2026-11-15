package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BoxShape = RoundedCornerShape(8.dp)

@Composable
fun DescDialogContent(
    describe: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(5.dp)
            .clip(BoxShape)
            .background(Color(0xFF1A2232), BoxShape)
            .border(1.dp, Color(0x22FFFFFF), BoxShape)
            .heightIn(max = 240.dp)
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        SelectionContainer {
            Text(
                text = describe,
                color = Color(0xFFFFFFFF),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun DescDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        DescDialogContent(
            describe = "这是一段影片简介详细信息。导演：某某，主演：某某。\n剧情简介：故事发生在一个阳光明媚的早晨……"
        )
    }
}
