package com.github.tvbox.osc.ui.compose

import com.fbox.mobile.MobileIconAction
import com.github.tvbox.osc.R

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ShapeRadius = RoundedCornerShape(10.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DanmuApiDialogContent(
    initialValue: String,
    hintText: String,
    onDefaultClick: () -> Unit,
    onSubmitClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(initialValue) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(15.dp)
    ) {
        Text(
            text = "弹幕地址",
            color = Color(0xFFFFFFFF),
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(hintText, color = Color(0x80FFFFFF), fontSize = 15.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onSubmitClick(text.trim()) }
            ),
            shape = ShapeRadius,
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
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            MobileIconAction(R.drawable.ic_action_reset, "恢复默认弹幕地址", onDefaultClick, caption = "默认")
            MobileIconAction(R.drawable.ic_action_save, "保存弹幕地址", { onSubmitClick(text.trim()) }, caption = "保存")
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun DanmuApiDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        DanmuApiDialogContent(
            initialValue = "http://example.com/danmu",
            hintText = "请输入弹幕搜索地址",
            onDefaultClick = {},
            onSubmitClick = {}
        )
    }
}
