package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun BackupDialogContent(
    backupList: List<String>,
    onBackupNow: () -> Unit,
    onStoragePermission: () -> Unit,
    onRestore: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(ShapeRadius)
                    .background(Color(0xFF1A2232), ShapeRadius)
                    .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                    .clickable(onClick = onBackupNow),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("立即备份", color = Color.White, fontSize = 14.sp)
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(ShapeRadius)
                    .background(Color(0xFF1A2232), ShapeRadius)
                    .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                    .clickable(onClick = onStoragePermission),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("存储权限", color = Color.White, fontSize = 14.sp)
            }
        }
        Text(
            text = "点击下方按钮操作",
            color = Color.White,
            fontSize = 15.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 264.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(backupList) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(ShapeRadius)
                            .background(Color(0xFF1A2232), ShapeRadius)
                            .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item,
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onRestore(item) }
                        )
                        Text(
                            text = "删除",
                            color = Color(0xFFFFB4AB),
                            fontSize = 13.sp,
                            modifier = Modifier
                                .clickable { onDelete(item) }
                                .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun BackupDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        BackupDialogContent(
            backupList = listOf("2026-09-14-120000", "2026-09-10-100000"),
            onBackupNow = {},
            onStoragePermission = {},
            onRestore = {},
            onDelete = {}
        )
    }
}
