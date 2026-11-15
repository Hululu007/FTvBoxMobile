package com.github.tvbox.osc.ui.dialog

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.api.DanmakuApi
import com.github.tvbox.osc.bean.DanmuSearchResult
import com.github.tvbox.osc.ui.compose.SearchDanmuDialogContent
import java.util.ArrayList

class SearchDanmuDialog(context: Context) : BaseDialog(context) {
    private var danmuLoader: DanmuLoader? = null
    private var episode: String = ""

    private var searchWordText by mutableStateOf("")
    private var resultsList by mutableStateOf<List<DanmuSearchResult>>(emptyList())
    private var isLoadingState by mutableStateOf(false)

    init {
        sheetLayoutName = "dialog_search_danmu"
        if (context is Activity) {
            setOwnerActivity(context)
        }
        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    SearchDanmuDialogContent(
                        searchWord = searchWordText,
                        onSearchWordChange = { searchWordText = it },
                        onSearch = { word -> search(word) },
                        results = resultsList,
                        isLoading = isLoadingState,
                        onItemClick = { item -> loadDanmu(item) }
                    )
                }
            }
        }
        setContentView(composeView)
    }

    fun setEpisode(episode: String?) {
        this.episode = episode ?: ""
    }

    fun setSearchWord(word: String?) {
        val searchWord = word?.trim() ?: ""
        searchWordText = searchWord
        search(searchWord)
    }

    fun setDanmuLoader(danmuLoader: DanmuLoader?) {
        this.danmuLoader = danmuLoader
    }

    private fun search(word: String) {
        resultsList = emptyList()
        if (TextUtils.isEmpty(word)) {
            Toast.makeText(context, "输入内容不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        isLoadingState = true
        DanmakuApi.searchList(word, episode, object : DanmakuApi.SearchListCallback {
            override fun onSuccess(results: List<DanmuSearchResult>) {
                isLoadingState = false
                resultsList = results
                if (results.isEmpty()) {
                    Toast.makeText(context, "未查询到匹配弹幕", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(message: String) {
                isLoadingState = false
                resultsList = emptyList()
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadDanmu(result: DanmuSearchResult) {
        isLoadingState = true
        DanmakuApi.loadSearchResult(result, object : DanmakuApi.SearchResultCallback {
            override fun onSuccess(danmu: String) {
                danmuLoader?.loadDanmu(danmu)
                dismiss()
            }

            override fun onError(message: String) {
                isLoadingState = false
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        DanmakuApi.cancel()
        dismiss()
    }

    interface DanmuLoader {
        fun loadDanmu(danmu: String)
    }
}
