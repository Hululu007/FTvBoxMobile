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

private val ButtonShape = RoundedCornerShape(10.dp)

@Composable
fun ConfirmDialogContent(
    message: String,
    confirmText: String = "清空",
    cancelText: String = "取消",
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = message,
            color = Color(0xFFFFFFFF),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Cancel button
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
                Text(
                    text = cancelText,
                    color = Color(0xFFFFFFFF),
                    fontSize = 15.sp
                )
            }
            // Confirm button
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(ButtonShape)
                    .background(Color(0xFF1A2232), ButtonShape)
                    .border(1.dp, Color(0x22FFFFFF), ButtonShape)
                    .clickable(onClick = onConfirm),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = confirmText,
                    color = Color(0xFFFFB4AB),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun ConfirmDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        ConfirmDialogContent(
            message = "确定全部删除 ?",
            onConfirm = {},
            onCancel = {}
        )
    }
}
