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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun SubtitleDialogContent(
    showInternal: Boolean,
    onSelectInternal: () -> Unit,
    onSelectLocal: () -> Unit,
    onSelectRemote: () -> Unit,
    sizeText: String,
    onSizeMinus: () -> Unit,
    onSizePlus: () -> Unit,
    styleOneText: String,
    styleTwoText: String,
    styleTwoColor: Color,
    positionText: String,
    onStyleOneClick: () -> Unit,
    onStyleTwoClick: () -> Unit,
    delayHintText: String,
    delayTimeText: String,
    onDelayMinus: () -> Unit,
    onDelayPlus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(bottom = 16.dp)
    ) {
        // Section 1: 字幕来源
        Text("字幕来源", color = Color(0xFF8E9DAE), fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (showInternal) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clip(ShapeRadius)
                        .background(Color(0xFF1A2232), ShapeRadius)
                        .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                        .clickable(onClick = onSelectInternal),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("内置字幕", color = Color.White, fontSize = 13.sp)
                }
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(ShapeRadius)
                    .background(Color(0xFF1A2232), ShapeRadius)
                    .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                    .clickable(onClick = onSelectLocal),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("本地字幕", color = Color.White, fontSize = 13.sp)
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(ShapeRadius)
                    .background(Color(0xFF1A2232), ShapeRadius)
                    .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                    .clickable(onClick = onSelectRemote),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("在线搜索", color = Color.White, fontSize = 13.sp)
            }
        }

        // Section 2: 字号大小
        Text("字号大小", color = Color(0xFF8E9DAE), fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(ShapeRadius)
                .background(Color(0xFF1A2232), ShapeRadius)
                .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                .padding(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .width(60.dp)
                    .height(38.dp)
                    .clip(ShapeRadius)
                    .background(Color(0xFF131822), ShapeRadius)
                    .clickable(onClick = onSizeMinus),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("－", color = Color(0xFF82B1FF), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(sizeText, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier
                    .width(60.dp)
                    .height(38.dp)
                    .clip(ShapeRadius)
                    .background(Color(0xFF131822), ShapeRadius)
                    .clickable(onClick = onSizePlus),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("＋", color = Color(0xFF82B1FF), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Section 3: 样式与位置
        Text("样式与位置", color = Color(0xFF8E9DAE), fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(ShapeRadius)
                    .background(Color(0xFF1A2232), ShapeRadius)
                    .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                    .clickable(onClick = onStyleOneClick),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(styleOneText, color = Color.White, fontSize = 13.sp)
            }
            if (positionText.isNotEmpty()) {
                Text(
                    text = positionText,
                    color = Color(0xFF8E9DAE),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(ShapeRadius)
                    .background(Color(0xFF1A2232), ShapeRadius)
                    .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                    .clickable(onClick = onStyleTwoClick),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(styleTwoText, color = styleTwoColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Section 4: 字幕延时
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("字幕延时", color = Color(0xFF8E9DAE), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(
                text = delayHintText,
                color = Color(0xFF5E6F82),
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(ShapeRadius)
                .background(Color(0xFF1A2232), ShapeRadius)
                .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                .padding(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .clip(ShapeRadius)
                    .background(Color(0xFF131822), ShapeRadius)
                    .clickable(onClick = onDelayMinus),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("提前 0.5s", color = Color(0xFF82B1FF), fontSize = 13.sp)
            }
            Row(
                modifier = Modifier.width(72.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(delayTimeText, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .clip(ShapeRadius)
                    .background(Color(0xFF131822), ShapeRadius)
                    .clickable(onClick = onDelayPlus),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("推迟 0.5s", color = Color(0xFF82B1FF), fontSize = 13.sp)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun SubtitleDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        SubtitleDialogContent(
            showInternal = false,
            onSelectInternal = {},
            onSelectLocal = {},
            onSelectRemote = {},
            sizeText = "16",
            onSizeMinus = {},
            onSizePlus = {},
            styleOneText = "字幕样式一",
            styleTwoText = "字幕样式二",
            styleTwoColor = Color(0xFFFFB6C1),
            positionText = "",
            onStyleOneClick = {},
            onStyleTwoClick = {},
            delayHintText = "仅对外挂字幕有效",
            delayTimeText = "0",
            onDelayMinus = {},
            onDelayPlus = {}
        )
    }
}
