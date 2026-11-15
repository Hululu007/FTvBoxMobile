package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// mobile_surface: bg #1A2232, 10dp radius, stroke #22FFFFFF
private val KeyboardItemShape = RoundedCornerShape(10.dp)
private val KeyboardItemBg = Color(0xFF1A2232)
private val KeyboardItemStroke = Color(0x22FFFFFF)

@Composable
fun KeyboardItem(
    keyText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(2.dp)
            .clip(KeyboardItemShape)
            .background(KeyboardItemBg, KeyboardItemShape)
            .border(1.dp, KeyboardItemStroke, KeyboardItemShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = keyText,
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF10131B)
@Composable
fun PreviewKeyboardItem() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        KeyboardItem(keyText = "A", onClick = {})
    }
}
