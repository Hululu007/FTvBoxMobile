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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ButtonShape = RoundedCornerShape(10.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiDialogContent(
    apiText: String,
    onApiTextChange: (String) -> Unit,
    liveApiText: String,
    onLiveApiTextChange: (String) -> Unit,
    onPasteApi: () -> Unit,
    onSelectFileApi: () -> Unit,
    onPasteLive: () -> Unit,
    onSelectFileLive: () -> Unit,
    onLiveHistory: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(bottom = 10.dp)
    ) {
        // 点播订阅
        Text(
            text = "点播订阅",
            color = Color(0xFFE6EAF2),
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = apiText,
            onValueChange = onApiTextChange,
            placeholder = { Text("输入或粘贴点播配置地址", color = Color(0xFFA6B1C5), fontSize = 15.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next
            ),
            shape = ButtonShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF131822),
                unfocusedContainerColor = Color(0xFF131822),
                focusedBorderColor = Color(0xFF00C2FF),
                unfocusedBorderColor = Color(0x22FFFFFF)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(ButtonShape)
                    .background(Color(0xFF1A2232), ButtonShape)
                    .border(1.dp, Color(0x22FFFFFF), ButtonShape)
                    .clickable(onClick = onPasteApi),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("粘贴", color = Color.White, fontSize = 14.sp)
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(ButtonShape)
                    .background(Color(0xFF1A2232), ButtonShape)
                    .border(1.dp, Color(0x22FFFFFF), ButtonShape)
                    .clickable(onClick = onSelectFileApi),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("选择文件", color = Color.White, fontSize = 14.sp)
            }
        }

        // 直播订阅
        Text(
            text = "直播订阅",
            color = Color(0xFFE6EAF2),
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 14.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = liveApiText,
            onValueChange = onLiveApiTextChange,
            placeholder = { Text("留空时使用点播配置", color = Color(0xFFA6B1C5), fontSize = 15.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onConfirm() }
            ),
            shape = ButtonShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF131822),
                unfocusedContainerColor = Color(0xFF131822),
                focusedBorderColor = Color(0xFF00C2FF),
                unfocusedBorderColor = Color(0x22FFFFFF)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(ButtonShape)
                    .background(Color(0xFF1A2232), ButtonShape)
                    .border(1.dp, Color(0x22FFFFFF), ButtonShape)
                    .clickable(onClick = onPasteLive),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("粘贴", color = Color.White, fontSize = 14.sp)
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(ButtonShape)
                    .background(Color(0xFF1A2232), ButtonShape)
                    .border(1.dp, Color(0x22FFFFFF), ButtonShape)
                    .clickable(onClick = onSelectFileLive),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("选择文件", color = Color.White, fontSize = 14.sp)
            }
        }

        // 直播配置历史
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(48.dp)
                .clip(ButtonShape)
                .background(Color(0xFF1A2232), ButtonShape)
                .border(1.dp, Color(0x22FFFFFF), ButtonShape)
                .clickable(onClick = onLiveHistory),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("直播配置历史", color = Color.White, fontSize = 14.sp)
        }

        // Cancel / Save buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(ButtonShape)
                    .background(Color(0xFF1A2232), ButtonShape)
                    .border(1.dp, Color(0x22FFFFFF), ButtonShape)
                    .clickable(onClick = onCancel),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("取消", color = Color.White, fontSize = 14.sp)
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(ButtonShape)
                    .background(Color(0xFF1E3860), ButtonShape)
                    .border(1.dp, Color(0x5900C2FF), ButtonShape)
                    .clickable(onClick = onConfirm),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("保存并更新", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun ApiDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        ApiDialogContent(
            apiText = "http://example.com/api",
            onApiTextChange = {},
            liveApiText = "",
            onLiveApiTextChange = {},
            onPasteApi = {},
            onSelectFileApi = {},
            onPasteLive = {},
            onSelectFileLive = {},
            onLiveHistory = {},
            onCancel = {},
            onConfirm = {}
        )
    }
}
