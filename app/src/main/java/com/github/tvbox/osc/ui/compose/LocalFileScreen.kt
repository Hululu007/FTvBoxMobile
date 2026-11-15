package com.github.tvbox.osc.ui.compose

import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.tvbox.osc.R
import com.owen.tvrecyclerview.widget.TvRecyclerView

@Composable
fun LocalFileScreen(
    title: String = "选择点播配置文件",
    path: String = "/storage/emulated/0/TVBox",
    isEmpty: Boolean = false,
    fileListView: TvRecyclerView? = null,
    onSetupFileList: ((TvRecyclerView) -> Unit)? = null
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFAEC6FF),
            background = Color(0xFF10131A),
            surface = Color(0xFF171C26),
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF10131A))
                .padding(16.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 19.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = path,
                color = Color(0xCCFFFFFF),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (fileListView != null) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = {
                            (fileListView.parent as? android.view.ViewGroup)?.removeView(fileListView)
                            fileListView
                        }
                    )
                } else {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            TvRecyclerView(ctx).apply {
                                id = R.id.fileList
                                clipChildren = true
                                clipToPadding = false
                                onSetupFileList?.invoke(this)
                            }
                        }
                    )
                }

                if (isEmpty) {
                    Text(
                        text = "当前目录为空",
                        color = Color(0xCCFFFFFF),
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LocalFileScreenPreview() {
    LocalFileScreen(isEmpty = true)
}
