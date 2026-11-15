package com.github.tvbox.osc.ui.tv.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.ui.compose.DEFAULT_KEYBOARD_KEYS
import com.github.tvbox.osc.ui.compose.KeyboardLayout

/**
 * @author pj567
 * @date :2020/12/23
 * @description:
 */
class SearchKeyboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var searchKeyListener: OnSearchKeyListener? = null

    init {
        initView()
    }

    private fun initView() {
        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    KeyboardLayout(
                        onKeyClick = { pos, key ->
                            searchKeyListener?.onSearchKey(pos, key)
                        }
                    )
                }
            }
        }
        addView(composeView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }

    fun setOnSearchKeyListener(listener: OnSearchKeyListener?) {
        searchKeyListener = listener
    }

    fun interface OnSearchKeyListener {
        fun onSearchKey(pos: Int, key: String)
    }
}
