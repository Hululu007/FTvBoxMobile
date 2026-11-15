package com.github.tvbox.osc.ui.compose

import android.widget.EditText
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.tvbox.osc.R

@Composable
fun PushScreen(
    urlState: MutableState<String> = remember { mutableStateOf("") },
    onPasteClick: () -> Unit = {},
    onPlayClick: () -> Unit = {},
    inputView: EditText? = null,
    onSetupInput: ((EditText) -> Unit)? = null
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFAEC6FF),
            background = Color(0xFF10131A),
            surface = Color(0xFF171C26),
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF10131A))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "播放链接",
                    color = Color(0xFFE6EAF2),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (inputView != null) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        factory = {
                            (inputView.parent as? android.view.ViewGroup)?.removeView(inputView)
                            inputView
                        }
                    )
                } else {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        factory = { ctx ->
                            EditText(ctx).apply {
                                id = R.id.mobileLinkInput
                                hint = "输入或粘贴视频、网页链接"
                                setHintTextColor(android.graphics.Color.parseColor("#A6B1C5"))
                                setTextColor(android.graphics.Color.WHITE)
                                textSize = 16f
                                setPadding(36, 0, 36, 0)
                                setBackgroundResource(R.drawable.mobile_input)
                                setSingleLine(true)
                                onSetupInput?.invoke(this)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1A2232))
                            .clickable { onPasteClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "粘贴链接",
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1E3860))
                            .clickable { onPlayClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "播放",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PushScreenPreview() {
    PushScreen()
}
