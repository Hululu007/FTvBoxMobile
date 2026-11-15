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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tvbox.osc.dlna.CastDevice

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun CastDialogContent(
    devices: List<CastDevice>,
    isSearching: Boolean,
    onDeviceClick: (CastDevice) -> Unit,
    onRefresh: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "投屏到设备",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "确认电视/盒子已安装TVBox或已打开DLNA投屏\n电视/盒子与手机连接在同一Wi-Fi下",
            color = Color(0x80FFFFFF),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .heightIn(min = 110.dp, max = 220.dp),
            contentAlignment = Alignment.Center
        ) {
            if (devices.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(devices) { device ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(ShapeRadius)
                                .background(Color(0xFF1A2232), ShapeRadius)
                                .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
                                .clickable { onDeviceClick(device) }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = device.name ?: "",
                                color = Color.White,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = (if (device.type == CastDevice.TYPE_DLNA) "DLNA " else "TVBox ") + device.id,
                                color = Color(0x80FFFFFF),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else if (isSearching) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF02F8E1),
                        strokeWidth = 2.dp
                    )
                    Text(
                        text = "正在搜索设备...",
                        color = Color(0x80FFFFFF),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            } else {
                Text(
                    text = "未找到可用设备",
                    color = Color(0x80FFFFFF),
                    fontSize = 14.sp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            MobileIconAction(R.drawable.ic_action_refresh, "刷新投屏设备", onRefresh)
            MobileIconAction(R.drawable.ic_sheet_close, "取消投屏", onCancel)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun CastDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        CastDialogContent(
            devices = emptyList(),
            isSearching = true,
            onDeviceClick = {},
            onRefresh = {},
            onCancel = {}
        )
    }
}
