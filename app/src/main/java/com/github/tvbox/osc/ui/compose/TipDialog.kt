package com.github.tvbox.osc.ui.compose

import com.fbox.mobile.MobileIconAction
import com.github.tvbox.osc.R

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun TipDialogContent(
    tip: String,
    leftText: String,
    rightText: String,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
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
            text = tip,
            color = Color(0xFFFFFFFF),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            MobileIconAction(if (leftText == "重试") R.drawable.ic_action_refresh else R.drawable.ic_mobile_check,
                leftText, onLeftClick, caption = leftText)
            MobileIconAction(R.drawable.ic_sheet_close, rightText, onRightClick, caption = rightText)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun TipDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        TipDialogContent(
            tip = "提示信息内容",
            leftText = "确定",
            rightText = "取消",
            onLeftClick = {},
            onRightClick = {}
        )
    }
}
