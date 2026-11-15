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
fun LibraryMobileItem(
    name: String,
    sourceOrYear: String? = null,
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
            .padding(bottom = 8.dp)
            .clip(ShapeRadius)
            .background(Color(0xFF1A2232), ShapeRadius)
            .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        setPadding(0, 0, 0, 0)
                    }
                },
                update = { ivThumb ->
                    val cleanPic = pic?.trim() ?: ""
                    if (!TextUtils.isEmpty(cleanPic)) {
                        ImgUtil.load(
                            cleanPic,
                            ivThumb,
                            AutoSizeUtils.mm2px(ivThumb.context, 10f),
                            AutoSizeUtils.mm2px(ivThumb.context, ImgUtil.defaultWidth.toFloat()),
                            AutoSizeUtils.mm2px(ivThumb.context, ImgUtil.defaultHeight.toFloat()),
                            name
                        )
                    } else {
                        ivThumb.setImageDrawable(ImgUtil.createTextDrawable(name))
                    }
                },
                modifier = Modifier
                    .size(width = 66.dp, height = 92.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 6.dp)
                )
                if (!sourceOrYear.isNullOrEmpty()) {
                    Text(
                        text = sourceOrYear,
                        color = Color.White,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
                if (!note.isNullOrEmpty()) {
                    Text(
                        text = note,
                        color = Color.White,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }

        // Delete overlay
        if (isDeleteMode) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .matchParentSize()
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

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun LibraryMobileItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(modifier = Modifier.padding(16.dp)) {
            LibraryMobileItem(
                name = "流浪地球2",
                sourceOrYear = "官源",
                note = "观看到 01:23:45",
                isDeleteMode = false
            )
        }
    }
}
