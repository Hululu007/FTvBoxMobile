package com.github.tvbox.osc.ui.dialog

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.R
import com.github.tvbox.osc.bean.Movie
import com.github.tvbox.osc.event.RefreshEvent
import com.github.tvbox.osc.ui.compose.QuickSearchDialogContent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

class QuickSearchDialog(context: Context) : BaseDialog(context, R.style.CustomDialogStyleDim) {
    private var wordsList by mutableStateOf<List<String>>(emptyList())
    private var resultsList by mutableStateOf<List<Movie.Video>>(emptyList())

    init {
        sheetLayoutName = "dialog_quick_search"
        setCanceledOnTouchOutside(false)
        setCancelable(true)

        EventBus.getDefault().register(this)
        setOnDismissListener {
            EventBus.getDefault().unregister(this)
        }

        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    QuickSearchDialogContent(
                        words = wordsList,
                        results = resultsList,
                        onWordClick = { word ->
                            resultsList = emptyList()
                            EventBus.getDefault().post(RefreshEvent(RefreshEvent.TYPE_QUICK_SEARCH_WORD_CHANGE, word))
                        },
                        onVideoClick = { video ->
                            EventBus.getDefault().post(RefreshEvent(RefreshEvent.TYPE_QUICK_SEARCH_SELECT, video))
                            dismiss()
                        }
                    )
                }
            }
        }
        setContentView(composeView)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refresh(event: RefreshEvent) {
        if (event.type == RefreshEvent.TYPE_QUICK_SEARCH) {
            val data = event.obj as? List<Movie.Video>
            if (data != null) {
                val current = ArrayList(resultsList)
                current.addAll(data)
                resultsList = current
            }
        } else if (event.type == RefreshEvent.TYPE_QUICK_SEARCH_WORD) {
            val data = event.obj as? List<String>
            if (data != null) {
                wordsList = data
            }
        }
    }
}
