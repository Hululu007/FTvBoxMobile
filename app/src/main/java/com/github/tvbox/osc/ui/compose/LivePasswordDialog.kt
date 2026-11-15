package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ShapeRadius = RoundedCornerShape(10.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivePasswordDialogContent(
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("请输入密码", color = Color(0x80FFFFFF), fontSize = 15.sp) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    val p = password.trim()
                    if (p.isNotEmpty()) onSubmit(p)
                }
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
                .weight(1f)
                .height(56.dp)
        )
        Row(
            modifier = Modifier
                .width(80.dp)
                .height(56.dp)
                .clip(ShapeRadius)
                .background(Color(0xFF1A2232), ShapeRadius)
                .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                .clickable {
                    val p = password.trim()
                    if (p.isNotEmpty()) onSubmit(p)
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "确定",
                color = Color(0xFFFFFFFF),
                fontSize = 15.sp
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun LivePasswordDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        LivePasswordDialogContent(onSubmit = {})
    }
}
