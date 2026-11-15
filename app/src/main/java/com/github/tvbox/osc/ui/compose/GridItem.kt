package com.github.tvbox.osc.ui.compose

import android.text.TextUtils
import android.widget.ImageView
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
import androidx.compose.ui.viewinterop.AndroidView
import com.github.tvbox.osc.R
import com.github.tvbox.osc.util.ImgUtil
import me.jessyan.autosize.utils.AutoSizeUtils

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun GridItem(
    name: String,
    year: String? = null,
    note: String? = null,
    pic: String? = null,
    isDeleteMode: Boolean = false,
    onClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(ShapeRadius)
            .background(Color(0xFF1A2232), ShapeRadius)
            .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
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
                update = { ivThumb ->
                    val cleanPic = pic?.trim() ?: ""
                    if (!TextUtils.isEmpty(cleanPic)) {
                        if (ImgUtil.isBase64Image(cleanPic)) {
                            ivThumb.setImageBitmap(ImgUtil.decodeBase64ToBitmap(cleanPic))
                        } else {
                            ImgUtil.load(
                                cleanPic,
                                ivThumb,
                                AutoSizeUtils.mm2px(ivThumb.context, 10f),
                                AutoSizeUtils.mm2px(ivThumb.context, ImgUtil.defaultWidth.toFloat()),
                                AutoSizeUtils.mm2px(ivThumb.context, ImgUtil.defaultHeight.toFloat()),
                                name
                            )
                        }
                    } else {
                        ivThumb.setImageDrawable(ImgUtil.createTextDrawable(name))
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Top badge (year)
            if (!year.isNullOrEmpty() && year != "0") {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .align(Alignment.TopStart)
                        .background(Color(0x99000000), RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp))
                        .padding(horizontal = 2.dp)
                ) {
                    Text(
                        text = year,
                        color = Color.White,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Bottom overlay (note + title)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
            ) {
                if (!note.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(start = 2.dp, end = 2.dp, bottom = 2.dp)
                            .background(Color(0xFF3D3D3D), RoundedCornerShape(2.dp))
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
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 42.dp)
                        .background(Color(0x99000000), RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                        .padding(2.dp)
                ) {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Delete overlay
            if (isDeleteMode) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x99000000))
                        .clickable(onClick = onDeleteClick)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_delete),
                        contentDescription = "Delete",
                        modifier = Modifier
                            .size(44.dp)
                            .padding(bottom = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun GridItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(modifier = Modifier.width(160.dp).padding(8.dp)) {
            GridItem(name = "电影大片", year = "2024", note = "4K超清")
        }
    }
}
