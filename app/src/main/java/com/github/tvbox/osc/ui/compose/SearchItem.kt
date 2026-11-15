package com.github.tvbox.osc.ui.compose

import android.widget.ImageView
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.tvbox.osc.util.ImgUtil
import me.jessyan.autosize.utils.AutoSizeUtils

/**
 * Composable for item_search.xml
 * FrameLayout matching mobile_surface background with poster, site tag, note tag, and title.
 */
@Composable
fun SearchItem(
    name: String,
    siteName: String = "",
    note: String = "",
    picUrl: String? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color(0xFF1A2232), shape)
            .border(1.dp, Color(0x22FFFFFF), shape)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(156.dp)
        ) {
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.FIT_XY
                        setPadding(0, 0, 0, 0)
                    }
                },
                update = { imageView ->
                    val pic = picUrl?.trim()
                    if (!pic.isNullOrEmpty()) {
                        ImgUtil.load(
                            pic,
                            imageView,
                            AutoSizeUtils.mm2px(imageView.context, 10f),
                            AutoSizeUtils.mm2px(imageView.context, 180f),
                            AutoSizeUtils.mm2px(imageView.context, 240f),
                            name
                        )
                    } else {
                        imageView.setImageDrawable(ImgUtil.createTextDrawable(name))
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (siteName.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(start = 2.dp, top = 2.dp, end = 2.dp)
                        .align(Alignment.TopStart)
                        .background(Color(0xFF1890FF), RoundedCornerShape(4.dp))
                        .padding(horizontal = 2.dp)
                ) {
                    Text(
                        text = siteName,
                        color = Color.White,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
            ) {
                if (note.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(start = 2.dp, end = 2.dp, bottom = 2.dp)
                            .background(Color(0xFF3D3D3D), RoundedCornerShape(4.dp))
                            .padding(horizontal = 2.dp)
                    ) {
                        Text(
                            text = note,
                            color = Color.White,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0x99000000),
                            RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                        )
                        .padding(2.dp)
                        .heightIn(min = 42.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun SearchItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        SearchItem(
            name = "流浪地球2",
            siteName = "豆瓣",
            note = "HD国语",
            picUrl = null
        )
    }
}
