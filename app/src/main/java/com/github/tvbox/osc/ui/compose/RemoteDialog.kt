package com.github.tvbox.osc.ui.compose

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RemoteDialogContent(
    address: String,
    qrBitmap: Bitmap?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (qrBitmap != null) {
            Image(
                bitmap = qrBitmap.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier
                    .size(136.dp)
                    .padding(bottom = 12.dp)
            )
        }
        Text(
            text = "手机/电脑扫描上方二维码或者直接浏览器访问地址\n$address",
            color = Color(0xFFFFFFFF),
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1D202A)
@Composable
private fun RemoteDialogPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        RemoteDialogContent(
            address = "http://192.168.1.100:9978",
            qrBitmap = null
        )
    }
}
