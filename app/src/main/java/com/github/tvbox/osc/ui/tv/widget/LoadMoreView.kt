package com.github.tvbox.osc.ui.tv.widget

import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.github.tvbox.osc.ui.compose.LoadMoreItem
import com.github.tvbox.osc.ui.compose.LoadMoreStatus

class LoadMoreView : LoadMoreView() {

    override fun getLayoutId(): Int {
        return 0
    }

    override fun getLoadingViewId(): Int {
        return 0
    }

    override fun getLoadFailViewId(): Int {
        return 0
    }

    override fun getLoadEndViewId(): Int {
        return 0
    }

    override fun convert(holder: BaseViewHolder) {
        val composeView = holder.itemView as? ComposeView ?: return
        val status = when (loadMoreStatus) {
            STATUS_LOADING -> LoadMoreStatus.LOADING
            STATUS_FAIL -> LoadMoreStatus.FAIL
            STATUS_END -> if (isLoadEndMoreGone) LoadMoreStatus.GONE else LoadMoreStatus.END
            else -> LoadMoreStatus.GONE
        }

        composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                LoadMoreItem(
                    status = status,
                    onRetryClick = {
                        if (loadMoreStatus == STATUS_FAIL) {
                            // Trigger retry via click
                            holder.itemView.performClick()
                        }
                    }
                )
            }
        }
    }

    companion object {
        fun createLoadingView(parent: ViewGroup): View {
            return ComposeView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            }
        }
    }
}
