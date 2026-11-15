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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.github.tvbox.osc.bean.Subtitle
import com.github.tvbox.osc.ui.compose.SearchSubtitleDialogContent
import com.github.tvbox.osc.viewmodel.SubtitleViewModel
import java.util.ArrayList

class SearchSubtitleDialog(private val mContext: Context) : BaseDialog(mContext) {

    private var mSubtitleLoader: SubtitleLoader? = null
    private var subtitleViewModel: SubtitleViewModel? = null
    private var page = 1
    private val maxPage = 5
    private var searchWord = ""

    private var zipSubtitles: MutableList<Subtitle> = ArrayList()
    private var isSearchPag = true

    private var searchWordText by mutableStateOf("")
    private var subtitlesList by mutableStateOf<List<Subtitle>>(emptyList())
    private var isLoadingState by mutableStateOf(false)

    init {
        sheetLayoutName = "dialog_search_subtitle"
        if (mContext is Activity) {
            setOwnerActivity(mContext)
        }
        val composeView = ComposeView(mContext).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    SearchSubtitleDialogContent(
                        searchWord = searchWordText,
                        onSearchWordChange = { searchWordText = it },
                        onSearch = { wd -> search(wd) },
                        subtitles = subtitlesList,
                        isLoading = isLoadingState,
                        onSubtitleClick = { subtitle ->
                            if (mSubtitleLoader != null) {
                                if (subtitle.getIsZip()) {
                                    isSearchPag = false
                                    isLoadingState = true
                                    subtitleViewModel?.getSearchResultSubtitleUrls(subtitle)
                                } else {
                                    loadSubtitle(subtitle)
                                    dismiss()
                                }
                            }
                        }
                    )
                }
            }
        }
        setContentView(composeView)
        initViewModel()
    }

    fun setSearchWord(wd: String) {
        var cleanWd = wd.replace(Regex("(?:（|\\(|\\[|【|\\.mp4|\\.mkv|\\.avi|\\.MP4|\\.MKV|\\.AVI)"), "")
        cleanWd = cleanWd.replace(Regex("(?:：|\\:|）|\\)|\\]|】|\\.)"), " ")
        val len = cleanWd.length
        val finalLen = if (len >= 36) 36 else len
        cleanWd = cleanWd.substring(0, finalLen).trim()
        searchWordText = cleanWd
        search(cleanWd)
    }

    fun search(wd: String) {
        isSearchPag = true
        subtitlesList = emptyList()
        if (!TextUtils.isEmpty(wd)) {
            isLoadingState = true
            searchWord = wd
            page = 1
            subtitleViewModel?.searchResult(wd, page)
        } else {
            Toast.makeText(context, "输入内容不能为空", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViewModel() {
        val vm = ViewModelProvider(mContext as ViewModelStoreOwner).get(SubtitleViewModel::class.java)
        subtitleViewModel = vm
        vm.searchResult.observe(mContext as LifecycleOwner, Observer { subtitleData ->
            if (subtitleData == null) return@Observer
            val data = subtitleData.getSubtitleList()
            isLoadingState = false
            if (data == null) {
                Toast.makeText(context, "未查询到匹配字幕", Toast.LENGTH_SHORT).show()
                return@Observer
            }

            if (data.isNotEmpty()) {
                if (subtitleData.getIsZip() == true) {
                    if (subtitleData.getIsNew() == true) {
                        subtitlesList = data
                        zipSubtitles = ArrayList(data)
                    } else {
                        val current = ArrayList(subtitlesList)
                        current.addAll(data)
                        subtitlesList = current
                        zipSubtitles.addAll(data)
                    }
                    page++
                } else {
                    subtitlesList = data
                }
            }
        })
    }

    private fun loadSubtitle(subtitle: Subtitle) {
        val loader = mSubtitleLoader ?: return
        subtitleViewModel?.getSubtitleUrl(subtitle, loader)
    }

    fun setSubtitleLoader(subtitleLoader: SubtitleLoader?) {
        mSubtitleLoader = subtitleLoader
    }

    interface SubtitleLoader {
        fun loadSubtitle(subtitle: Subtitle)
    }

    override fun onBackPressed() {
        if (!isSearchPag) {
            isSearchPag = true
            isLoadingState = false
            subtitlesList = zipSubtitles
            return
        }
        dismiss()
    }
}
