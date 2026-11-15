package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tvbox.osc.R

private val SurfaceShape = RoundedCornerShape(10.dp)
private val SurfaceBg = Color(0xFF1A2232)
private val SurfaceStroke = Color(0x22FFFFFF)
private val BadgeBg = Color(0x66000000)

@Composable
fun PlayerVodControlView(
    modifier: Modifier = Modifier,
    // Top Info
    showTopContainer: Boolean = false,
    titleText: String = "http://",
    videoResolutionText: String = "1024 x 768",
    netSpeedText: String = "0Gbs",
    seekTimeText: String = "00:00:00/00:00",
    sysTimeText: String = "00:00:00",
    // Center Indicators
    isLoading: Boolean = false,
    netSpeedLoadingText: String = "0",
    isPaused: Boolean = false,
    pauseText: String = "100",
    isSlideSeeking: Boolean = false,
    slideText: String = "100",
    showSpeed3Indicator: Boolean = false,
    // Bottom Controls
    showBottomContainer: Boolean = false,
    currentTimeText: String = "00:00",
    totalTimeText: String = "00:00",
    currentProgress: Float = 0f,
    playScaleText: String = "16:9",
    playSpeedText: String = "x1.0",
    playerTypeText: String = "EXO播放器",
    decodeTypeText: String = "硬解",
    // Callbacks
    onSeek: (Float) -> Unit = {},
    onPlayNextClick: () -> Unit = {},
    onPlayPreClick: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {},
    onScaleClick: () -> Unit = {},
    onSpeedClick: () -> Unit = {},
    onPlayerChangeClick: () -> Unit = {},
    onDecodeChangeClick: () -> Unit = {},
    onSubtitleClick: () -> Unit = {},
    onCastClick: () -> Unit = {},
    onScreenDisplayClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onLockClick: () -> Unit = {},
    isLocked: Boolean = false,
    showLockBtn: Boolean = false,
    showBackBtn: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // TOP CONTAINER
        if (showTopContainer) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Top Left
                Column(modifier = Modifier.weight(3f, fill = false)) {
                    Text(
                        text = titleText,
                        color = Color.White,
                        fontSize = 12.sp,
                        maxLines = 1,
                        modifier = Modifier
                            .background(BadgeBg, RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = videoResolutionText,
                        color = Color.White,
                        fontSize = 12.sp,
                        maxLines = 1,
                        modifier = Modifier
                            .background(BadgeBg, RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

                // Top Right
                Column(
                    modifier = Modifier.weight(1f, fill = false),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        modifier = Modifier
                            .background(BadgeBg, RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = netSpeedText, color = Color.White, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = seekTimeText, color = Color.White, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = sysTimeText, color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }

        // SPEED 3X INDICATOR
        if (showSpeed3Indicator) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .background(Color(0xCC000000), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "3.0 X",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // CENTER PAUSE
        if (isPaused) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(SurfaceShape)
                    .background(SurfaceBg, SurfaceShape)
                    .border(1.dp, SurfaceStroke, SurfaceShape)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.exo_icon_play),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = pauseText,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        // CENTER SLIDE INFO
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

        // CENTER LOADING
        if (isLoading) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    color = Color(0xFF02F8E1),
                    strokeWidth = 3.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = netSpeedLoadingText,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }

        // BACK BUTTON
        if (showBackBtn) {
            Image(
                painter = painterResource(id = R.drawable.icon_back),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterStart)
                    .clickable { onBackClick() }
                    .padding(12.dp)
            )
        }

        // LOCK BUTTON
        if (showLockBtn) {
            Image(
                painter = painterResource(
                    id = if (isLocked) R.drawable.icon_lock else R.drawable.icon_unlock
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterEnd)
                    .clickable { onLockClick() }
                    .padding(12.dp)
            )
        }

        // BOTTOM CONTROLS CONTAINER
        if (showBottomContainer) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color(0xCC10131B))
                    .padding(12.dp)
            ) {
                // Button Group Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "播放",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    val actionButtons = listOf(
                        "下一集" to onPlayNextClick,
                        "上一集" to onPlayPreClick,
                        "重播" to onRetryClick,
                        "刷新" to onRefreshClick,
                        playScaleText to onScaleClick,
                        playSpeedText to onSpeedClick,
                        playerTypeText to onPlayerChangeClick,
                        decodeTypeText to onDecodeChangeClick,
                        "投屏" to onCastClick,
                        "字幕" to onSubtitleClick,
                        "屏显" to onScreenDisplayClick
                    )

                    for ((label, action) in actionButtons) {
                        Box(
                            modifier = Modifier
                                .height(48.dp)
                                .padding(end = 8.dp)
                                .clip(SurfaceShape)
                                .background(SurfaceBg, SurfaceShape)
                                .border(1.dp, SurfaceStroke, SurfaceShape)
                                .clickable { action() }
                                .padding(horizontal = 14.dp, vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = Color.White,
                                fontSize = 14.sp,
                                maxLines = 1
                            )
                        }
                    }
                }

                // Seek Progress Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentTimeText,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.width(48.dp),
                        textAlign = TextAlign.End
                    )

                    Slider(
                        value = currentProgress.coerceIn(0f, 1f),
                        onValueChange = onSeek,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF02F8E1),
                            activeTrackColor = Color(0xFF02F8E1),
                            inactiveTrackColor = Color(0x33FFFFFF)
                        )
                    )

                    Text(
                        text = totalTimeText,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.width(48.dp),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 450)
@Composable
fun PreviewPlayerVodControlView() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        PlayerVodControlView(
            showTopContainer = true,
            showBottomContainer = true,
            currentProgress = 0.4f,
            currentTimeText = "15:20",
            totalTimeText = "45:00",
            titleText = "测试电影.mp4"
        )
    }
}
