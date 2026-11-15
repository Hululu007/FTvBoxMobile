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
fun DanmuSettingDialogContent(
    onSearchDanmuClick: () -> Unit,
    speed: Float,
    onSpeedChange: (Float) -> Unit,
    sizeScale: Float,
    onSizeChange: (Float) -> Unit,
    maxLine: Int,
    onLineChange: (Int) -> Unit,
    alpha: Float,
    onAlphaChange: (Float) -> Unit,
    useRandomColor: Boolean,
    onColorChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(bottom = 16.dp)
    ) {
        // 在线弹幕搜索
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
                .height(44.dp)
                .clip(ShapeRadius)
                .background(Color(0xFF1E3860), ShapeRadius)
                .border(1.dp, Color(0x5900C2FF), ShapeRadius)
                .clickable(onClick = onSearchDanmuClick),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("搜索在线弹幕 ›", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        // 弹幕速度
        Text("弹幕速度", color = Color(0xFF8E9DAE), fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
        val speeds = listOf(2.4f, 1.8f, 1.5f, 1.0f)
        val speedLabels = listOf("超慢", "慢", "适中", "快")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            speeds.forEachIndexed { index, s ->
                val isSelected = kotlin.math.abs(speed - s) < 0.05f
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clip(ShapeRadius)
                        .background(if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232), ShapeRadius)
                        .border(1.dp, if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF), ShapeRadius)
                        .clickable { onSpeedChange(s) },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        speedLabels[index],
                        color = if (isSelected) Color(0xFFAEC6FF) else Color.White,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // 弹幕字号
        Text("弹幕字号", color = Color(0xFF8E9DAE), fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
        StepperRow(
            valueText = "${kotlin.math.round(sizeScale * 10).toInt()}档",
            onMinus = {
                val current = kotlin.math.round(sizeScale * 10).toInt()
                if (current > 6) onSizeChange((current - 1) / 10f)
            },
            onPlus = {
                val current = kotlin.math.round(sizeScale * 10).toInt()
                if (current < 20) onSizeChange((current + 1) / 10f)
            },
            modifier = Modifier.padding(bottom = 14.dp)
        )

        // 弹幕行数
        Text("弹幕行数", color = Color(0xFF8E9DAE), fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
        StepperRow(
            valueText = "${maxLine}行",
            onMinus = {
                if (maxLine > 1) onLineChange(maxLine - 1)
            },
            onPlus = {
                if (maxLine < 15) onLineChange(maxLine + 1)
            },
            modifier = Modifier.padding(bottom = 14.dp)
        )

        // 弹幕透明度
        Text("弹幕透明度", color = Color(0xFF8E9DAE), fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
        StepperRow(
            valueText = "${kotlin.math.round(alpha * 100).toInt()}%",
            onMinus = {
                val current = kotlin.math.round(alpha * 100).toInt()
                if (current > 10) onAlphaChange((current - 10) / 100f)
            },
            onPlus = {
                val current = kotlin.math.round(alpha * 100).toInt()
                if (current < 100) onAlphaChange((current + 10) / 100f)
            },
            modifier = Modifier.padding(bottom = 14.dp)
        )

        // 弹幕颜色
        Text("弹幕颜色", color = Color(0xFF8E9DAE), fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(false to "默认", true to "随机").forEach { (rand, label) ->
                val isSelected = useRandomColor == rand
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clip(ShapeRadius)
                        .background(if (isSelected) Color(0xFF1E3860) else Color(0xFF1A2232), ShapeRadius)
                        .border(1.dp, if (isSelected) Color(0x5900C2FF) else Color(0x22FFFFFF), ShapeRadius)
                        .clickable { onColorChange(rand) },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        label,
                        color = if (isSelected) Color(0xFFAEC6FF) else Color.White,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun StepperRow(
    valueText: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
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
                .clickable(onClick = onMinus),
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
            Text(valueText, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Row(
            modifier = Modifier
                .width(60.dp)
                .height(38.dp)
                .clip(ShapeRadius)
                .background(Color(0xFF131822), ShapeRadius)
                .clickable(onClick = onPlus),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("＋", color = Color(0xFF82B1FF), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun DanmuSettingDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        DanmuSettingDialogContent(
            onSearchDanmuClick = {},
            speed = 1.5f,
            onSpeedChange = {},
            sizeScale = 1.0f,
            onSizeChange = {},
            maxLine = 8,
            onLineChange = {},
            alpha = 1.0f,
            onAlphaChange = {},
            useRandomColor = false,
            onColorChange = {}
        )
    }
}
