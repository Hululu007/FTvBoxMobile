package com.github.tvbox.osc.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tvbox.osc.R

private val SurfaceShape = RoundedCornerShape(10.dp)
private val SurfaceBg = Color(0xFF1A2232)
private val SurfaceStroke = Color(0x22FFFFFF)

@Composable
fun PlayerLiveControlView(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isPaused: Boolean = false,
    pauseText: String = "100",
    isSlideSeeking: Boolean = false,
    slideText: String = "100",
    onSingleTap: () -> Unit = {},
    onLongPress: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable(onClick = onSingleTap)
    ) {
        // Pause indicator
        if (isPaused) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .size(width = 110.dp, height = 77.dp)
                        .clip(SurfaceShape)
                        .background(SurfaceBg, SurfaceShape)
                        .border(1.dp, SurfaceStroke, SurfaceShape),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.exo_icon_play),
                        contentDescription = null,
                        modifier = Modifier.size(33.dp)
                    )
                    if (pauseText.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = pauseText,
                            color = Color.White,
                            fontSize = 19.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Slide info text
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

        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF02F8E1),
                    strokeWidth = 3.dp
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 450)
@Composable
fun PreviewPlayerLiveControlView() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        PlayerLiveControlView(
            isLoading = true,
            isPaused = true,
            pauseText = "暂停"
        )
    }
}
