package com.fbox.mobile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.github.tvbox.osc.R
import com.github.tvbox.osc.base.ScreenController
import com.github.tvbox.osc.ui.activity.*

/** The app shell is owned entirely by Compose; content controllers never add navigation bars. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobileFrame(
    screen: ScreenController?,
    route: String,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    MaterialTheme(colorScheme = darkColorScheme(
        primary = Color(0xFFAEC6FF),
        background = Color(0xFF10131A),
        surface = Color(0xFF171C26),
    )) {
        val fullscreen = screen?.fullscreen == true
        val roots = listOf("home", "search", "live", "setting")
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                if (!fullscreen && route !in listOf("home", "search", "fast_search", "web_login")) {
                    TopAppBar(
                        title = { Text(title(route, screen), maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回") } },
                        actions = {
                            if (route == "history" || route == "collect") {
                                MobileIconAction(R.drawable.ic_action_manage, "管理", { screen?.findViewById<android.view.View>(R.id.tvDelete)?.performClick() })
                                MobileIconAction(R.drawable.icon_delete, "清空", { screen?.findViewById<android.view.View>(R.id.tvClear)?.performClick() })
                            }
                        },
                        windowInsets = WindowInsets(0, 0, 0, 0)
                    )
                }
            },
            bottomBar = {
                if (!fullscreen && route in roots) {
                    NavigationBar(windowInsets = WindowInsets(0, 0, 0, 0)) {
                        val icons = listOf(Icons.Default.Home, Icons.Default.Search, Icons.Default.LiveTv, Icons.Default.Settings)
                        val labels = listOf("首页", "搜索", "直播", "设置")
                        roots.forEachIndexed { index, destination ->
                            NavigationBarItem(
                                selected = route == destination,
                                onClick = { onNavigate(destination) },
                                icon = { Icon(icons[index], labels[index]) },
                                label = { Text(labels[index]) }
                            )
                        }
                    }
                }
            }
        ) { padding -> content(Modifier.fillMaxSize().padding(padding)) }
    }
}

private fun title(route: String, screen: ScreenController?): String = when (route) {
    "detail" -> screen?.intent?.getStringExtra("title")?.takeIf { it.isNotBlank() } ?: "影片详情"
    "setting" -> "设置"
    "history" -> "观看历史"
    "collect" -> "收藏"
    "local_file" -> "本地文件"
    "push" -> "链接播放"
    "live" -> "电视直播"
    else -> "FBox"
}

@Preview(showBackground = true)
@Composable
private fun MobileFramePreview() {
    MobileFrame(null, "home", {}, {}) { modifier -> Box(modifier) }
}
