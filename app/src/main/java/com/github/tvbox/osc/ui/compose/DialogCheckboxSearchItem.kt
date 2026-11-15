package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun DialogCheckboxSearchItem(
    name: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 2.dp)
            .clip(ShapeRadius)
            .background(Color(0xFF1A2232), ShapeRadius)
            .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
            .clickable { onCheckedChange(!isChecked) }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFAEC6FF),
                    uncheckedColor = Color(0x80FFFFFF),
                    checkmarkColor = Color(0xFF10131A)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = name,
                color = Color(0xFFFFFFFF),
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun DialogCheckboxSearchItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(modifier = Modifier.padding(8.dp)) {
            DialogCheckboxSearchItem(name = "百度源", isChecked = true, onCheckedChange = {})
            DialogCheckboxSearchItem(name = "阿里源", isChecked = false, onCheckedChange = {})
        }
    }
}
