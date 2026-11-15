package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tvbox.osc.R

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun DialogBackupItem(
    name: String,
    onNameClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = 48.dp)
                .clip(ShapeRadius)
                .background(Color(0xFF1A2232), ShapeRadius)
                .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                .clickable(onClick = onNameClick)
                .padding(5.dp)
        ) {
            Text(
                text = name,
                color = Color(0xFFFFFFFF),
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(ShapeRadius)
                .background(Color(0xFF1A2232), ShapeRadius)
                .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                .clickable(onClick = onDeleteClick)
                .padding(5.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_delete),
                contentDescription = "Delete",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun DialogBackupItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(modifier = Modifier.padding(8.dp)) {
            DialogBackupItem(name = "backup_20240101.json")
        }
    }
}
