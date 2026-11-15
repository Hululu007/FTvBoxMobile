package com.github.tvbox.osc.ui.compose

import com.fbox.mobile.MobileIconAction
import com.github.tvbox.osc.R

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tvbox.osc.bean.SourceBean

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun CheckboxSearchDialogContent(
    sourceList: List<SourceBean>,
    checkedSources: Map<String, String>?,
    onSourceToggle: (SourceBean, Boolean) -> Unit,
    onCheckAll: () -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MobileIconAction(R.drawable.ic_action_select_all, "全选", onCheckAll)
            MobileIconAction(R.drawable.ic_action_deselect, "全不选", onClearAll)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 360.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sourceList) { source ->
                    val isChecked = checkedSources?.containsKey(source.key) == true
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(ShapeRadius)
                            .background(
                                if (isChecked) Color(0xFF1E3860) else Color(0xFF1A2232),
                                ShapeRadius
                            )
                            .border(
                                1.dp,
                                if (isChecked) Color(0x5900C2FF) else Color(0x22FFFFFF),
                                ShapeRadius
                            )
                            .clickable { onSourceToggle(source, !isChecked) }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { onSourceToggle(source, it) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF00C2FF),
                                checkmarkColor = Color.White,
                                uncheckedColor = Color(0x80FFFFFF)
                            )
                        )
                        Text(
                            text = source.name ?: "",
                            color = if (isChecked) Color(0xFFAEC6FF) else Color.White,
                            fontSize = 13.sp,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun CheckboxSearchDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        CheckboxSearchDialogContent(
            sourceList = emptyList(),
            checkedSources = null,
            onSourceToggle = { _, _ -> },
            onCheckAll = {},
            onClearAll = {}
        )
    }
}
