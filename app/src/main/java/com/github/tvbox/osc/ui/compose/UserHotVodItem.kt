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
fun UserHotVodItem(
    name: String,
    rate: String? = null,
    note: String? = null,
    pic: String? = null,
    isPhoneListMode: Boolean = false,
    isDeleteMode: Boolean = false,
    onClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val outerShape = ShapeRadius
    if (isPhoneListMode) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clip(outerShape)
                .background(Color(0xFF1A2232), outerShape)
                .border(1.dp, Color(0x22FFFFFF), outerShape)
                .clickable(onClick = onClick)
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 112.dp)
                    .clip(RoundedCornerShape(6.dp))
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
                                    AutoSizeUtils.mm2px(ivThumb.context, 160f),
                                    AutoSizeUtils.mm2px(ivThumb.context, 224f),
                                    name
                                )
                            }
                        } else {
                            ivThumb.setImageDrawable(ImgUtil.createTextDrawable(name))
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
                if (!rate.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.TopStart)
                            .background(Color(0x99000000), RoundedCornerShape(2.dp))
                            .padding(horizontal = 2.dp)
                    ) {
                        Text(
                            text = rate,
                            color = Color.White,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (!note.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(start = 1.dp, end = 1.dp, bottom = 2.dp)
                            .align(Alignment.BottomStart)
                            .background(Color(0xFF3D3D3D), RoundedCornerShape(2.dp))
                            .padding(horizontal = 2.dp)
                    ) {
                        Text(
                            text = note,
                            color = Color.White,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
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
            Text(
                text = name,
                color = Color.White,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(outerShape)
                .background(Color(0xFF1A2232), outerShape)
                .border(1.dp, Color(0x22FFFFFF), outerShape)
                .clickable(onClick = onClick)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp)
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
                if (!rate.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.TopStart)
                            .background(Color(0x99000000), RoundedCornerShape(2.dp))
                            .padding(horizontal = 2.dp)
                    ) {
                        Text(
                            text = rate,
                            color = Color.White,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (!note.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(start = 1.dp, end = 1.dp, bottom = 2.dp)
                            .align(Alignment.BottomStart)
                            .background(Color(0xFF3D3D3D), RoundedCornerShape(2.dp))
                            .padding(horizontal = 2.dp)
                    ) {
                        Text(
                            text = note,
                            color = Color.White,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
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
            Text(
                text = name,
                color = Color.White,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 42.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun UserHotVodItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(modifier = Modifier.width(160.dp).padding(8.dp)) {
            UserHotVodItem(
                name = "热门电影推荐",
                rate = "8.9",
                note = "高清中字"
            )
        }
    }
}
