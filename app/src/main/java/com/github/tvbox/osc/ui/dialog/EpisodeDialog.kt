package com.github.tvbox.osc.ui.dialog

import android.app.Activity
import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.bean.VodInfo
import com.github.tvbox.osc.ui.compose.EpisodeDialogContent
import java.util.ArrayList

class EpisodeDialog(
    context: Context,
    title: String?,
    episodes: List<VodInfo.VodSeries>?,
    selectedPosition: Int,
    listener: EpisodeSelectListener?
) : BaseDialog(context) {

    fun interface EpisodeSelectListener {
        fun selectEpisode(position: Int)
    }

    private val episodes: List<VodInfo.VodSeries>
    private val selectedPosition: Int
    private val listener: EpisodeSelectListener?

    init {
        sheetLayoutName = "dialog_episode"
        if (context is Activity) setOwnerActivity(context)
        this.episodes = if (episodes == null) ArrayList() else ArrayList(episodes)
        this.selectedPosition = Math.max(0, Math.min(selectedPosition, Math.max(0, this.episodes.size - 1)))
        this.listener = listener

        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    EpisodeDialogContent(
                        episodes = this@EpisodeDialog.episodes,
                        selectedIndex = this@EpisodeDialog.selectedPosition,
                        onEpisodeSelected = { position ->
                            dismiss()
                            this@EpisodeDialog.listener?.selectEpisode(position)
                        },
                        title = title
                    )
                }
            }
        }
        setContentView(composeView)
    }
}
