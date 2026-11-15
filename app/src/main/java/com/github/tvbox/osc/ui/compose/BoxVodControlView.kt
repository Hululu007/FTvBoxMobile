package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tvbox.osc.R

private val SurfaceShape = RoundedCornerShape(10.dp)
private val SurfaceBg = Color(0xFF1A2232)
private val SurfaceStroke = Color(0x22FFFFFF)
private val DialogBg = Color(0xCC000000)

@Composable
fun BoxVodControlView(
    modifier: Modifier = Modifier,
    videoSizeText: String = "16:9",
    videoSpeedText: String = "x1",
    isPlaying: Boolean = true,
    currentTimeText: String = "00:00",
    totalTimeText: String = "00:00",
    currentProgress: Float = 0f, // 0f to 1f
    infoTitle: String = "http://",
    infoHint: String = "点击选集切换剧集",
    isPaused: Boolean = false,
    pauseText: String = "100",
    isSlideSeeking: Boolean = false,
    slideText: String = "100",
    showCenterControls: Boolean = true,
    showBottomControls: Boolean = true,
    showTvBottomControls: Boolean = false,
    onPlayPauseClick: () -> Unit = {},
    onSeek: (Float) -> Unit = {},
    onPlayPreClick: () -> Unit = {},
    onPlayNextClick: () -> Unit = {},
    onVideoSizeClick: () -> Unit = {},
    onVideoSpeedClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Top Info Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Color(0x99000000))
                .padding(horizontal = 6.dp, vertical = 5.dp)
        ) {
            Text(
                text = infoTitle,
                color = Color.White,
                fontSize = 15.sp,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Text(
                text = infoHint,
                color = Color.White,
                fontSize = 15.sp,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        // Center / End container (video size & speed)
        if (showCenterControls) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .widthIn(min = 50.dp)
                        .clip(SurfaceShape)
                        .background(SurfaceBg, SurfaceShape)
                        .border(1.dp, SurfaceStroke, SurfaceShape)
                        .clickable { onVideoSizeClick() }
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = videoSizeText,
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Box(
                    modifier = Modifier
                        .widthIn(min = 50.dp)
                        .clip(SurfaceShape)
                        .background(SurfaceBg, SurfaceShape)
                        .border(1.dp, SurfaceStroke, SurfaceShape)
                        .clickable { onVideoSpeedClick() }
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = videoSpeedText,
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Center Pause container
        if (isPaused) {
            Column(
                modifier = Modifier
                    .size(width = 176.dp, height = 132.dp)
                    .align(Alignment.Center)
                    .clip(SurfaceShape)
                    .background(DialogBg, SurfaceShape)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.exo_icon_play),
                    contentDescription = null,
                    modifier = Modifier.size(66.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = pauseText,
                    color = Color.White,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Center Progress / Slide info container
        if (isSlideSeeking) {
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 55.dp)
                    .align(Alignment.Center)
                    .clip(SurfaceShape)
                    .background(SurfaceBg, SurfaceShape)
                    .border(1.dp, SurfaceStroke, SurfaceShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = slideText,
                    color = Color.White,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Bottom Container (Phone / standard style)
        if (showBottomControls) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color(0x99000000))
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(
                        id = if (isPlaying) R.drawable.dkplayer_selector_play_button else R.drawable.dkplayer_selector_play_button
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onPlayPauseClick() }
                        .padding(12.dp)
                )

                Text(
                    text = currentTimeText,
                    color = Color.White,
                    fontSize = 12.sp
                )

                Slider(
                    value = currentProgress.coerceIn(0f, 1f),
                    onValueChange = onSeek,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF02F8E1),
                        activeTrackColor = Color(0xFF02F8E1),
                        inactiveTrackColor = Color(0x33FFFFFF)
                    )
                )

                Text(
                    text = totalTimeText,
                    color = Color.White,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "上一集",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clickable { onPlayPreClick() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )

                Text(
                    text = "下一集",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clickable { onPlayNextClick() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }
        }

        // TV Bottom alternative container
        if (showTvBottomControls) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color(0x99000000))
                    .padding(horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(
                    "下一集" to onPlayNextClick,
                    "上一集" to onPlayPreClick,
                    videoSpeedText to onVideoSpeedClick,
                    videoSizeText to onVideoSizeClick
                ).forEach { (label, onClick) ->
                    Box(
                        modifier = Modifier
                            .size(width = 55.dp, height = 40.dp)
                            .padding(end = 4.dp)
                            .clip(SurfaceShape)
                            .background(SurfaceBg, SurfaceShape)
                            .border(1.dp, SurfaceStroke, SurfaceShape)
                            .clickable { onClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = Color.White,
                            fontSize = 14.sp,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 450)
@Composable
fun PreviewBoxVodControlView() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        BoxVodControlView(
            currentProgress = 0.35f,
            currentTimeText = "12:30",
            totalTimeText = "45:00",
            infoTitle = "测试剧集 第1集"
        )
    }
}
