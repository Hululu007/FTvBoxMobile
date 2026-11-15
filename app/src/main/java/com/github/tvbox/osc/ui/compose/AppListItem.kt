package com.github.tvbox.osc.ui.compose

import android.text.TextUtils
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.tvbox.osc.util.ImgUtil
import me.jessyan.autosize.utils.AutoSizeUtils

private val ShapeRadius = RoundedCornerShape(10.dp)

@Composable
fun AppListItem(
    name: String,
    note: String = "",
    pic: String? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .clip(ShapeRadius)
            .background(Color(0xFF1A2232), ShapeRadius)
            .border(1.dp, Color(0x22FFFFFF), ShapeRadius)
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 56.dp)
                .padding(horizontal = 8.dp, vertical = 4.dp)
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
                                AutoSizeUtils.mm2px(ivThumb.context, 240f),
                                AutoSizeUtils.mm2px(ivThumb.context, 336f),
                                name
                            )
                        }
                    } else {
                        ivThumb.setImageDrawable(ImgUtil.createTextDrawable(name))
                    }
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Text(
                text = name,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(3f)
                    .padding(5.dp)
            )
            if (note.isNotEmpty()) {
                Text(
                    text = note,
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117)
@Composable
private fun AppListItemPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(modifier = Modifier.padding(8.dp)) {
            AppListItem(name = "电影名称", note = "HD高清", pic = null)
        }
    }
}
