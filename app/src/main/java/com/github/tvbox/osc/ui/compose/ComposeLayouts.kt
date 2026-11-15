package com.github.tvbox.osc.ui.compose

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.tvbox.osc.R
import com.owen.tvrecyclerview.widget.TvRecyclerView

object ComposeLayouts {
    fun createCollectLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.rootLayout }
        val tvDelete = ImageView(context).apply { id = R.id.tvDelete; visibility = View.GONE }
        val tvClear = ImageView(context).apply { id = R.id.tvClear; visibility = View.GONE }
        val tvDelTip = TextView(context).apply { id = R.id.tvDelTip; visibility = View.GONE }
        val mGridView = TvRecyclerView(context).apply { id = R.id.mGridView; clipChildren = true; clipToPadding = false }

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CollectScreen(
                    onDeleteClick = { tvDelete.performClick() },
                    onClearClick = { tvClear.performClick() },
                    recyclerView = mGridView
                )
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        root.addView(tvDelete, FrameLayout.LayoutParams(0, 0))
        root.addView(tvClear, FrameLayout.LayoutParams(0, 0))
        root.addView(tvDelTip, FrameLayout.LayoutParams(0, 0))
        return root
    }

    fun createHistoryLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.rootLayout }
        val tvDelete = ImageView(context).apply { id = R.id.tvDelete; visibility = View.GONE }
        val tvClear = ImageView(context).apply { id = R.id.tvClear; visibility = View.GONE }
        val tvDelTip = TextView(context).apply { id = R.id.tvDelTip; visibility = View.GONE }
        val mGridView = TvRecyclerView(context).apply { id = R.id.mGridView; clipChildren = true; clipToPadding = false }

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                HistoryScreen(
                    onDeleteClick = { tvDelete.performClick() },
                    onClearClick = { tvClear.performClick() },
                    recyclerView = mGridView
                )
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        root.addView(tvDelete, FrameLayout.LayoutParams(0, 0))
        root.addView(tvClear, FrameLayout.LayoutParams(0, 0))
        root.addView(tvDelTip, FrameLayout.LayoutParams(0, 0))
        return root
    }

    fun createPushLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.rootLayout }
        val input = EditText(context).apply {
            id = R.id.mobileLinkInput
            hint = "输入或粘贴视频、网页链接"
            setHintTextColor(android.graphics.Color.parseColor("#A6B1C5"))
            setTextColor(android.graphics.Color.WHITE)
            textSize = 16f
            setBackgroundResource(R.drawable.mobile_input)
            setSingleLine(true)
            imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_GO or android.view.inputmethod.EditorInfo.IME_FLAG_NO_EXTRACT_UI
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_URI
        }
        val pushLocal = TextView(context).apply { id = R.id.pushLocal; visibility = View.GONE }
        val mobilePlayLink = TextView(context).apply { id = R.id.mobilePlayLink; visibility = View.GONE }
        val ivQRCode = ImageView(context).apply { id = R.id.ivQRCode; visibility = View.GONE }
        val tvAddress = TextView(context).apply { id = R.id.tvAddress; visibility = View.GONE }

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PushScreen(
                    onPasteClick = { pushLocal.performClick() },
                    onPlayClick = { mobilePlayLink.performClick() },
                    inputView = input
                )
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        root.addView(pushLocal, FrameLayout.LayoutParams(0, 0))
        root.addView(mobilePlayLink, FrameLayout.LayoutParams(0, 0))
        root.addView(ivQRCode, FrameLayout.LayoutParams(0, 0))
        root.addView(tvAddress, FrameLayout.LayoutParams(0, 0))
        return root
    }

    fun createLocalFileLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.rootLayout }
        val tvTitle = TextView(context).apply { id = R.id.tvTitle; visibility = View.GONE }
        val tvPath = TextView(context).apply { id = R.id.tvPath; visibility = View.GONE }
        val tvEmpty = TextView(context).apply { id = R.id.tvEmpty; visibility = View.GONE }
        val fileList = TvRecyclerView(context).apply { id = R.id.fileList; clipChildren = true; clipToPadding = false }

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LocalFileScreen(
                    title = tvTitle.text.toString().ifEmpty { "选择点播配置文件" },
                    path = tvPath.text.toString(),
                    isEmpty = tvEmpty.visibility == View.VISIBLE,
                    fileListView = fileList
                )
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        root.addView(tvTitle, FrameLayout.LayoutParams(0, 0))
        root.addView(tvPath, FrameLayout.LayoutParams(0, 0))
        root.addView(tvEmpty, FrameLayout.LayoutParams(0, 0))
        return root
    }

    fun createSettingLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.rootLayout }
        val mGridView = TvRecyclerView(context).apply { id = R.id.mGridView; clipChildren = true; clipToPadding = false; visibility = View.GONE }
        val mViewPager = com.github.tvbox.osc.ui.tv.widget.NoScrollViewPager(context).apply { id = R.id.mViewPager }

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SettingScreen(
                    showMenu = false,
                    menuRecyclerView = mGridView,
                    viewPager = mViewPager
                )
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        root.addView(mGridView, FrameLayout.LayoutParams(0, 0))
        return root
    }

    fun createGridLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.rootLayout }
        val mGridView = TvRecyclerView(context).apply { id = R.id.mGridView; clipChildren = true; clipToPadding = false }

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                GridScreen(recyclerView = mGridView)
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        return root
    }

    fun createPlayLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.rootLayout }
        val mVideoView = com.github.tvbox.osc.player.MyVideoView(context).apply { id = R.id.mVideoView }
        val danmaku = master.flame.danmaku.ui.widget.DanmakuView(context).apply { id = R.id.danmaku; visibility = View.GONE }
        val play_loading = ProgressBar(context).apply { id = R.id.play_loading }
        val play_load_error = ImageView(context).apply { id = R.id.play_load_error; visibility = View.GONE }
        val play_load_tip = TextView(context).apply { id = R.id.play_load_tip }

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PlayScreen(
                    videoView = mVideoView,
                    danmakuView = danmaku,
                    loadingView = play_loading,
                    errorView = play_load_error,
                    tipView = play_load_tip
                )
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        return root
    }

    fun createUserLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.rootLayout }
        val tvUserHome = LinearLayout(context).apply { id = R.id.tvUserHome; visibility = View.GONE }
        val mobileHomeActionsClip = FrameLayout(context).apply { id = R.id.mobileHomeActionsClip; visibility = View.GONE }
        val tvHistory = LinearLayout(context).apply { id = R.id.tvHistory; visibility = View.GONE }
        val tvLive = LinearLayout(context).apply { id = R.id.tvLive; visibility = View.GONE }
        val tvSearch = LinearLayout(context).apply { id = R.id.tvSearch; visibility = View.GONE }
        val tvPush = LinearLayout(context).apply { id = R.id.tvPush; visibility = View.GONE }
        val tvFavorite = LinearLayout(context).apply { id = R.id.tvFavorite; visibility = View.GONE }
        val tvSetting = LinearLayout(context).apply { id = R.id.tvSetting; visibility = View.GONE }
        val mobileLocal = LinearLayout(context).apply { id = R.id.mobileLocal; visibility = View.GONE }
        val tvHotList = TvRecyclerView(context).apply { id = R.id.tvHotList; clipChildren = true; clipToPadding = false }

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                UserScreen(
                    onHistoryClick = { tvHistory.performClick() },
                    onLiveClick = { tvLive.performClick() },
                    onSearchClick = { tvSearch.performClick() },
                    onSettingClick = { tvSetting.performClick() },
                    onLocalClick = { mobileLocal.performClick() },
                    onCollectClick = { tvFavorite.performClick() },
                    onPushClick = { tvPush.performClick() },
                    hotListView = tvHotList
                )
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        root.addView(tvUserHome, FrameLayout.LayoutParams(0, 0))
        root.addView(tvHistory, FrameLayout.LayoutParams(0, 0))
        root.addView(tvLive, FrameLayout.LayoutParams(0, 0))
        root.addView(tvSearch, FrameLayout.LayoutParams(0, 0))
        root.addView(tvPush, FrameLayout.LayoutParams(0, 0))
        root.addView(tvFavorite, FrameLayout.LayoutParams(0, 0))
        root.addView(tvSetting, FrameLayout.LayoutParams(0, 0))
        root.addView(mobileLocal, FrameLayout.LayoutParams(0, 0))
        root.addView(mobileHomeActionsClip, FrameLayout.LayoutParams(0, 0))
        return root
    }

    fun createHomeLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.rootLayout }
        val topLayout = LinearLayout(context).apply { id = R.id.topLayout; visibility = View.GONE }
        val tvName = TextView(context).apply { id = R.id.tvName; text = "FBox" }
        val tvDate = TextView(context).apply { id = R.id.tvDate; visibility = View.GONE }
        val contentLayout = LinearLayout(context).apply { id = R.id.contentLayout; visibility = View.GONE }
        val mGridView = TvRecyclerView(context).apply { id = R.id.mGridView; clipChildren = true; clipToPadding = false }
        val mViewPager = com.github.tvbox.osc.ui.tv.widget.NoScrollViewPager(context).apply { id = R.id.mViewPager }

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                HomeScreen(
                    appName = tvName.text.toString(),
                    onTitleClick = { tvName.performClick() },
                    menuRecyclerView = mGridView,
                    viewPager = mViewPager,
                    titleView = tvName
                )
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        root.addView(topLayout, FrameLayout.LayoutParams(0, 0))
        root.addView(tvDate, FrameLayout.LayoutParams(0, 0))
        root.addView(contentLayout, FrameLayout.LayoutParams(0, 0))
        return root
    }

    fun createFastSearchLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.rootLayout }
        val mobileSearchBack = ImageButton(context).apply { id = R.id.mobileSearchBack }
        val mSearchTitle = TextView(context).apply { id = R.id.mSearchTitle; text = "准备搜索\n结果 0" }
        val mGridViewWordFenci = TvRecyclerView(context).apply { id = R.id.mGridViewWordFenci; clipChildren = true; clipToPadding = false }
        val llWord = LinearLayout(context).apply { id = R.id.llWord; visibility = View.GONE }
        val mGridViewWord = TvRecyclerView(context).apply { id = R.id.mGridViewWord; clipChildren = true; clipToPadding = false }
        val llLayout = LinearLayout(context).apply { id = R.id.llLayout; visibility = View.GONE }
        val mGridView = TvRecyclerView(context).apply { id = R.id.mGridView; clipChildren = true; clipToPadding = false; visibility = View.INVISIBLE }
        val mGridViewFilter = TvRecyclerView(context).apply { id = R.id.mGridViewFilter; clipChildren = true; clipToPadding = false; visibility = View.INVISIBLE }

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FastSearchScreen(
                    backBtn = mobileSearchBack,
                    searchTitleView = mSearchTitle,
                    fenciRecyclerView = mGridViewWordFenci,
                    wordRecyclerView = mGridViewWord,
                    resultRecyclerView = mGridView,
                    filterRecyclerView = mGridViewFilter
                )
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        root.addView(llWord, FrameLayout.LayoutParams(0, 0))
        root.addView(llLayout, FrameLayout.LayoutParams(0, 0))
        return root
    }

    fun createSearchLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.rootLayout }
        val mobileSearchBack = ImageButton(context).apply { id = R.id.mobileSearchBack }
        val etSearch = EditText(context).apply { id = R.id.etSearch; setSingleLine(true) }
        val tvClear = TextView(context).apply { id = R.id.tvClear }
        val tvSearch = TextView(context).apply { id = R.id.tvSearch }
        val tvSearchCheckboxBtn = TextView(context).apply { id = R.id.tvSearchCheckboxBtn }
        val mobileSearchSuggestions = LinearLayout(context).apply { id = R.id.mobileSearchSuggestions; visibility = View.GONE }
        val llHistoryWord = LinearLayout(context).apply { id = R.id.llHistoryWord; visibility = View.GONE }
        val tvHistoryTitle = TextView(context).apply { id = R.id.tvHistoryTitle; visibility = View.GONE }
        val tvHistoryClear = TextView(context).apply { id = R.id.tvHistoryClear; visibility = View.GONE }
        val mobileSearchHistoryEmpty = TextView(context).apply { id = R.id.mobileSearchHistoryEmpty; visibility = View.GONE }
        val historyWordGrid = TvRecyclerView(context).apply { id = R.id.historyWordGrid; visibility = View.GONE }
        val llWord = LinearLayout(context).apply { id = R.id.llWord; visibility = View.GONE }
        val wordSwitch = TextView(context).apply { id = R.id.wordSwitch; visibility = View.GONE }
        val mGridViewWord = TvRecyclerView(context).apply { id = R.id.mGridViewWord; visibility = View.GONE }
        val mobileSearchResults = LinearLayout(context).apply { id = R.id.mobileSearchResults; visibility = View.GONE }
        val llLayout = LinearLayout(context).apply { id = R.id.llLayout; visibility = View.GONE }
        val mGridView = TvRecyclerView(context).apply { id = R.id.mGridView; clipChildren = true; clipToPadding = false }
        val keyBoardRoot = LinearLayout(context).apply { id = R.id.keyBoardRoot; visibility = View.GONE }
        val remoteRoot = LinearLayout(context).apply { id = R.id.remoteRoot; visibility = View.GONE }
        val tvAddress = TextView(context).apply { id = R.id.tvAddress; visibility = View.GONE }
        val ivQRCode = ImageView(context).apply { id = R.id.ivQRCode; visibility = View.GONE }

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SearchScreen(
                    backBtn = mobileSearchBack,
                    searchEdit = etSearch,
                    clearBtn = tvClear,
                    searchBtn = tvSearch,
                    checkboxBtn = tvSearchCheckboxBtn,
                    resultsGrid = mGridView
                )
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        root.addView(mobileSearchSuggestions, FrameLayout.LayoutParams(0, 0))
        root.addView(llHistoryWord, FrameLayout.LayoutParams(0, 0))
        root.addView(tvHistoryTitle, FrameLayout.LayoutParams(0, 0))
        root.addView(tvHistoryClear, FrameLayout.LayoutParams(0, 0))
        root.addView(mobileSearchHistoryEmpty, FrameLayout.LayoutParams(0, 0))
        root.addView(historyWordGrid, FrameLayout.LayoutParams(0, 0))
        root.addView(llWord, FrameLayout.LayoutParams(0, 0))
        root.addView(wordSwitch, FrameLayout.LayoutParams(0, 0))
        root.addView(mGridViewWord, FrameLayout.LayoutParams(0, 0))
        root.addView(mobileSearchResults, FrameLayout.LayoutParams(0, 0))
        root.addView(llLayout, FrameLayout.LayoutParams(0, 0))
        root.addView(keyBoardRoot, FrameLayout.LayoutParams(0, 0))
        root.addView(remoteRoot, FrameLayout.LayoutParams(0, 0))
        root.addView(tvAddress, FrameLayout.LayoutParams(0, 0))
        root.addView(ivQRCode, FrameLayout.LayoutParams(0, 0))
        return root
    }

    fun createDetailLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.rootLayout }
        val llLayout = LinearLayout(context).apply { id = R.id.llLayout; orientation = LinearLayout.VERTICAL }
        val topLayout = LinearLayout(context).apply { id = R.id.topLayout; orientation = LinearLayout.HORIZONTAL }
        val thumbContainer = FrameLayout(context).apply { id = R.id.thumbContainer }
        val ivThumb = ImageView(context).apply { id = R.id.ivThumb }
        val previewPlayerPlace = View(context).apply { id = R.id.previewPlayerPlace; visibility = View.GONE }
        val tvName = TextView(context).apply { id = R.id.tvName }
        val tvSite = TextView(context).apply { id = R.id.tvSite }
        val tvYear = TextView(context).apply { id = R.id.tvYear }
        val tvArea = TextView(context).apply { id = R.id.tvArea }
        val tvLang = TextView(context).apply { id = R.id.tvLang }
        val tvType = TextView(context).apply { id = R.id.tvType }
        val tvActor = TextView(context).apply { id = R.id.tvActor }
        val tvDirector = TextView(context).apply { id = R.id.tvDirector }
        val tvPlayUrl = TextView(context).apply { id = R.id.tvPlayUrl }
        val tvDes = TextView(context).apply { id = R.id.tvDes }
        val tvPlay = TextView(context).apply { id = R.id.tvPlay }
        val tvQuickSearch = TextView(context).apply { id = R.id.tvQuickSearch }
        val tvDesc = TextView(context).apply { id = R.id.tvDesc }
        val tvCollect = TextView(context).apply { id = R.id.tvCollect }
        val tvChangeSource = TextView(context).apply { id = R.id.tvChangeSource }
        val mEmptyPlaylist = LinearLayout(context).apply { id = R.id.mEmptyPlaylist; visibility = View.GONE }
        val mGridViewFlag = TvRecyclerView(context).apply { id = R.id.mGridViewFlag; clipChildren = true; clipToPadding = false }
        val mSeriesGroupTv = LinearLayout(context).apply { id = R.id.mSeriesGroupTv; visibility = View.GONE }
        val mSeriesSortTv = TextView(context).apply { id = R.id.mSeriesSortTv }
        val mSeriesGroupView = TvRecyclerView(context).apply { id = R.id.mSeriesGroupView; clipChildren = true; clipToPadding = false }
        val mGridViewQuality = TvRecyclerView(context).apply { id = R.id.mGridViewQuality; clipChildren = true; clipToPadding = false; visibility = View.GONE }
        val mGridView = TvRecyclerView(context).apply { id = R.id.mGridView; clipChildren = true; clipToPadding = false }
        val previewPlayer = androidx.fragment.app.FragmentContainerView(context).apply { id = R.id.previewPlayer; visibility = View.GONE }
        val previewPlayerBlock = View(context).apply { id = R.id.previewPlayerBlock; visibility = View.GONE }

        thumbContainer.addView(ivThumb)
        topLayout.addView(thumbContainer)
        topLayout.addView(previewPlayerPlace)
        topLayout.addView(tvName)
        topLayout.addView(tvSite)
        topLayout.addView(tvYear)
        topLayout.addView(tvArea)
        topLayout.addView(tvLang)
        topLayout.addView(tvType)
        topLayout.addView(tvActor)
        topLayout.addView(tvDirector)
        topLayout.addView(tvPlayUrl)
        topLayout.addView(tvDes)
        topLayout.addView(tvPlay)
        topLayout.addView(tvQuickSearch)
        topLayout.addView(tvDesc)
        topLayout.addView(tvCollect)
        topLayout.addView(tvChangeSource)

        mSeriesGroupTv.addView(mSeriesSortTv)
        mSeriesGroupTv.addView(mSeriesGroupView)

        llLayout.addView(topLayout)
        llLayout.addView(mEmptyPlaylist)
        llLayout.addView(mGridViewFlag)
        llLayout.addView(mSeriesGroupTv)
        llLayout.addView(mGridViewQuality)
        llLayout.addView(mGridView)

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DetailScreen(
                    topLayout = topLayout,
                    emptyPlaylist = mEmptyPlaylist,
                    flagGrid = mGridViewFlag,
                    seriesGroupTv = mSeriesGroupTv,
                    qualityGrid = mGridViewQuality,
                    mainGrid = mGridView,
                    previewPlayer = previewPlayer,
                    previewPlayerBlock = previewPlayerBlock
                )
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        root.addView(llLayout, FrameLayout.LayoutParams(0, 0))
        root.addView(previewPlayer, FrameLayout.LayoutParams(0, 0))
        root.addView(previewPlayerBlock, FrameLayout.LayoutParams(0, 0))
        return root
    }

    fun createLivePlayLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.live_root }
        val mVideoView = xyz.doikki.videoplayer.player.VideoView<xyz.doikki.videoplayer.player.AbstractPlayer>(context).apply { setId(R.id.mVideoView) }
        val switchChannelSnapshotOverlay = FrameLayout(context).apply { id = R.id.switchChannelSnapshotOverlay; visibility = View.GONE }
        val switchChannelSnapshotImage = ImageView(context).apply { id = R.id.switchChannelSnapshotImage; visibility = View.GONE }
        val tv_selected_channel = TextView(context).apply { id = R.id.tv_selected_channel; visibility = View.GONE }
        val tvLeftChannnelListLayout = LinearLayout(context).apply { id = R.id.tvLeftChannnelListLayout; visibility = View.GONE }
        val ll_typeSelect = LinearLayout(context).apply { id = R.id.ll_typeSelect }
        val mGroupGridView = TvRecyclerView(context).apply { id = R.id.mGroupGridView; clipChildren = true; clipToPadding = false }
        val divLoadEpgleft = LinearLayout(context).apply { id = R.id.divLoadEpgleft; visibility = View.GONE }
        val tv_arrow0 = TextView(context).apply { id = R.id.tv_arrow0 }
        val iv_arrow2 = ImageView(context).apply { id = R.id.iv_arrow2 }
        val tv_arrow3 = TextView(context).apply { id = R.id.tv_arrow3 }
        val mChannelGridView = TvRecyclerView(context).apply { id = R.id.mChannelGridView; clipChildren = true; clipToPadding = false }
        val divLoadEpgDivider = LinearLayout(context).apply { id = R.id.divLoadEpgDivider }
        val divLoadEpg = LinearLayout(context).apply { id = R.id.divLoadEpg }
        val tv_arrow = TextView(context).apply { id = R.id.tv_arrow }
        val iv_arrow = ImageView(context).apply { id = R.id.iv_arrow }
        val tv_arrow2 = TextView(context).apply { id = R.id.tv_arrow2 }
        val divEPG = LinearLayout(context).apply { id = R.id.divEPG; visibility = View.GONE }
        val mEpgDateGridView = TvRecyclerView(context).apply { id = R.id.mEpgDateGridView; clipChildren = true; clipToPadding = false }
        val lv_epg = TvRecyclerView(context).apply { id = R.id.lv_epg; clipChildren = true; clipToPadding = false }
        val txtNoEpg = TextView(context).apply { id = R.id.txtNoEpg; visibility = View.GONE }
        val tvRightSettingLayout = LinearLayout(context).apply { id = R.id.tvRightSettingLayout; visibility = View.GONE }
        val mSettingItemView = TvRecyclerView(context).apply { id = R.id.mSettingItemView; clipChildren = true; clipToPadding = false }
        val mSettingGroupView = TvRecyclerView(context).apply { id = R.id.mSettingGroupView; clipChildren = true; clipToPadding = false }
        val tvChannel = TextView(context).apply { id = R.id.tvChannel; visibility = View.GONE }
        val tv_top_status = TextView(context).apply { id = R.id.tv_top_status; visibility = View.GONE }
        val tvNetSpeed = TextView(context).apply { id = R.id.tvNetSpeed; visibility = View.GONE }
        val tvTime = TextView(context).apply { id = R.id.tvTime; visibility = View.GONE }
        val ll_epg = LinearLayout(context).apply { id = R.id.ll_epg; visibility = View.GONE }
        val channelinfo = LinearLayout(context).apply { id = R.id.channelinfo; visibility = View.GONE }
        val tv_channel_bottom_number = TextView(context).apply { id = R.id.tv_channel_bottom_number; visibility = View.GONE }
        val tv_channel_bar_name = TextView(context).apply { id = R.id.tv_channel_bar_name; visibility = View.GONE }
        val tv_current_program = TextView(context).apply { id = R.id.tv_current_program; visibility = View.GONE }
        val tv_current_program_time = TextView(context).apply { id = R.id.tv_current_program_time; visibility = View.GONE }
        val tv_current_program_name = TextView(context).apply { id = R.id.tv_current_program_name; visibility = View.GONE }
        val tv_next_program = TextView(context).apply { id = R.id.tv_next_program; visibility = View.GONE }
        val tv_next_program_time = TextView(context).apply { id = R.id.tv_next_program_time; visibility = View.GONE }
        val tv_next_program_name = TextView(context).apply { id = R.id.tv_next_program_name; visibility = View.GONE }
        val live_status_info = LinearLayout(context).apply { id = R.id.live_status_info; visibility = View.GONE }
        val tv_source = TextView(context).apply { id = R.id.tv_source; visibility = View.GONE }
        val tvResolution = TextView(context).apply { id = R.id.tvResolution; visibility = View.GONE }
        val tv_shownum = TextView(context).apply { id = R.id.tv_shownum; visibility = View.GONE }
        val tv_showTime = TextView(context).apply { id = R.id.tv_showTime; visibility = View.GONE }
        val ll_loading = LinearLayout(context).apply { id = R.id.ll_loading; visibility = View.GONE }
        val ll_right_top_huikan = LinearLayout(context).apply { id = R.id.ll_right_top_huikan; visibility = View.GONE }
        val iv_back_bg = ImageView(context).apply { id = R.id.iv_back_bg; visibility = View.GONE }
        val tv_right_top_epg_name = TextView(context).apply { id = R.id.tv_right_top_epg_name; visibility = View.GONE }
        val tv_right_top_type = TextView(context).apply { id = R.id.tv_right_top_type; visibility = View.GONE }
        val ll_right_top_loading = LinearLayout(context).apply { id = R.id.ll_right_top_loading; visibility = View.GONE }
        val iv_circle_bg = ImageView(context).apply { id = R.id.iv_circle_bg; visibility = View.GONE }
        val tv_right_top_channel_name = TextView(context).apply { id = R.id.tv_right_top_channel_name; visibility = View.GONE }
        val backcontroller = LinearLayout(context).apply { id = R.id.backcontroller; visibility = View.GONE }
        val ll_play = LinearLayout(context).apply { id = R.id.ll_play; visibility = View.GONE }
        val iv_play = ImageView(context).apply { id = R.id.iv_play; visibility = View.GONE }
        val progress_container = FrameLayout(context).apply { id = R.id.progress_container; visibility = View.GONE }
        val iv_playpause = ImageView(context).apply { id = R.id.iv_playpause; visibility = View.GONE }
        val tv_currentpos = TextView(context).apply { id = R.id.tv_currentpos; visibility = View.GONE }
        val pb_progressbar = ProgressBar(context).apply { id = R.id.pb_progressbar; visibility = View.GONE }
        val tv_duration = TextView(context).apply { id = R.id.tv_duration; visibility = View.GONE }

        switchChannelSnapshotOverlay.addView(switchChannelSnapshotImage)

        divLoadEpgleft.addView(tv_arrow0)
        divLoadEpgleft.addView(iv_arrow2)
        divLoadEpgleft.addView(tv_arrow3)

        divLoadEpg.addView(tv_arrow)
        divLoadEpg.addView(iv_arrow)
        divLoadEpg.addView(tv_arrow2)

        divEPG.addView(mEpgDateGridView)
        divEPG.addView(lv_epg)
        divEPG.addView(txtNoEpg)

        ll_typeSelect.addView(mGroupGridView)
        ll_typeSelect.addView(divLoadEpgleft)
        ll_typeSelect.addView(divLoadEpgDivider)
        ll_typeSelect.addView(mChannelGridView)
        ll_typeSelect.addView(divLoadEpg)
        ll_typeSelect.addView(divEPG)

        tvLeftChannnelListLayout.addView(ll_typeSelect)

        tvRightSettingLayout.addView(mSettingItemView)
        tvRightSettingLayout.addView(mSettingGroupView)

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LivePlayScreen(
                    videoView = mVideoView,
                    overlayView = switchChannelSnapshotOverlay,
                    channelTextView = tv_selected_channel,
                    leftChannelListLayout = tvLeftChannnelListLayout,
                    rightSettingLayout = tvRightSettingLayout,
                    statusInfoView = live_status_info,
                    loadingView = ll_loading,
                    rightTopHuikanView = ll_right_top_huikan,
                    backControllerView = backcontroller
                )
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        root.addView(tvChannel, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_top_status, FrameLayout.LayoutParams(0, 0))
        root.addView(tvNetSpeed, FrameLayout.LayoutParams(0, 0))
        root.addView(tvTime, FrameLayout.LayoutParams(0, 0))
        root.addView(ll_epg, FrameLayout.LayoutParams(0, 0))
        root.addView(channelinfo, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_channel_bottom_number, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_channel_bar_name, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_current_program, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_current_program_time, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_current_program_name, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_next_program, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_next_program_time, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_next_program_name, FrameLayout.LayoutParams(0, 0))
        root.addView(live_status_info, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_source, FrameLayout.LayoutParams(0, 0))
        root.addView(tvResolution, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_shownum, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_showTime, FrameLayout.LayoutParams(0, 0))
        root.addView(ll_loading, FrameLayout.LayoutParams(0, 0))
        root.addView(ll_right_top_huikan, FrameLayout.LayoutParams(0, 0))
        root.addView(iv_back_bg, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_right_top_epg_name, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_right_top_type, FrameLayout.LayoutParams(0, 0))
        root.addView(ll_right_top_loading, FrameLayout.LayoutParams(0, 0))
        root.addView(iv_circle_bg, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_right_top_channel_name, FrameLayout.LayoutParams(0, 0))
        root.addView(backcontroller, FrameLayout.LayoutParams(0, 0))
        root.addView(ll_play, FrameLayout.LayoutParams(0, 0))
        root.addView(iv_play, FrameLayout.LayoutParams(0, 0))
        root.addView(progress_container, FrameLayout.LayoutParams(0, 0))
        root.addView(iv_playpause, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_currentpos, FrameLayout.LayoutParams(0, 0))
        root.addView(pb_progressbar, FrameLayout.LayoutParams(0, 0))
        root.addView(tv_duration, FrameLayout.LayoutParams(0, 0))
        return root
    }

    fun createModelLayout(context: Context): View {
        val root = FrameLayout(context).apply { id = R.id.rootLayout }
        val scrollView = ScrollView(context).apply {
            id = View.generateViewId()
            isFocusable = false
        }
        val linear = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        val ids = intArrayOf(
            R.id.llDebug, R.id.tvDebugOpen, R.id.llApi, R.id.tvApi, R.id.llApiRightGroup,
            R.id.llApiHistory, R.id.tvMediaCodec1, R.id.llApiLine, R.id.tvApiLine,
            R.id.llHomeApi, R.id.tvHomeApi, R.id.tvHomeLive, R.id.tvHomeText,
            R.id.llHomeRec, R.id.tvHomeRec, R.id.llHomeRecStyle, R.id.showRecStyleText,
            R.id.llSearchView, R.id.tvSearchView, R.id.showFastSearch, R.id.showFastSearchText,
            R.id.llPlay, R.id.tvPlay, R.id.llMediaCodec, R.id.tvMediaCodec, R.id.m3u8Ad, R.id.m3u8AdText,
            R.id.danmuOpen, R.id.danmuOpenText, R.id.danmuApi, R.id.danmuApiText,
            R.id.llRender, R.id.tvRenderType, R.id.autoSwitchLine, R.id.autoSwitchLineText,
            R.id.llDns, R.id.tvDns, R.id.llParseWebVew, R.id.tvParseWebView,
            R.id.llHistoryMerge, R.id.tvHistoryMerge, R.id.llHistoryNum, R.id.tvHistoryNum,
            R.id.llWp, R.id.llWpRecovery, R.id.llScale, R.id.tvScaleType,
            R.id.showPreview, R.id.showPreviewText, R.id.llIjkCachePlay, R.id.tvIjkCachePlay,
            R.id.llClearCache, R.id.llBackup, R.id.llAbout
        )
        for (idVal in ids) {
            val v = LinearLayout(context).apply { id = idVal }
            linear.addView(v, LinearLayout.LayoutParams(-1, 0))
        }
        scrollView.addView(linear, ViewGroup.LayoutParams(-1, -2))

        val composeView = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ModelScreen(scrollView = scrollView)
            }
        }
        root.addView(composeView, FrameLayout.LayoutParams(-1, -1))
        root.addView(scrollView, FrameLayout.LayoutParams(0, 0))
        return root
    }

    fun createActivityLayout(context: Context, layoutResId: Int): View? {
        return when (layoutResId) {
            R.layout.activity_collect -> createCollectLayout(context)
            R.layout.activity_history -> createHistoryLayout(context)
            R.layout.activity_push -> createPushLayout(context)
            R.layout.activity_local_file -> createLocalFileLayout(context)
            R.layout.activity_setting -> createSettingLayout(context)
            R.layout.activity_home -> createHomeLayout(context)
            R.layout.activity_fast_search -> createFastSearchLayout(context)
            R.layout.activity_search -> createSearchLayout(context)
            R.layout.activity_detail -> createDetailLayout(context)
            R.layout.activity_live_play -> createLivePlayLayout(context)
            else -> null
        }
    }

    fun createFragmentLayout(context: Context, layoutResId: Int): View? {
        return when (layoutResId) {
            R.layout.fragment_grid -> createGridLayout(context)
            R.layout.fragment_user -> createUserLayout(context)
            R.layout.fragment_model -> createModelLayout(context)
            R.layout.activity_play -> createPlayLayout(context)
            else -> null
        }
    }
}
