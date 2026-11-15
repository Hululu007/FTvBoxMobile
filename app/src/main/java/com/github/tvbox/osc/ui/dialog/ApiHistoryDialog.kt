package com.github.tvbox.osc.ui.dialog

import android.content.Context
import android.os.Bundle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.R
import com.github.tvbox.osc.ui.adapter.ApiHistoryDialogAdapter
import com.github.tvbox.osc.ui.compose.ApiHistoryDialogContent
import java.util.ArrayList

class ApiHistoryDialog(context: Context) : BaseDialog(context, R.style.CustomDialogStyleDim) {

    private var itemsList by mutableStateOf<List<String>>(emptyList())
    private var selectedIdx by mutableStateOf(0)
    private var titleText by mutableStateOf("配置历史")
    private var callback: ApiHistoryDialogAdapter.SelectDialogInterface? = null

    init {
        sheetLayoutName = "dialog_api_history"
        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    ApiHistoryDialogContent(
                        items = itemsList,
                        selectedIndex = selectedIdx,
                        title = titleText,
                        onItemClick = { item, index ->
                            selectedIdx = index
                            callback?.click(item)
                        },
                        onDeleteClick = { item, index ->
                            val current = ArrayList(itemsList)
                            current.removeAt(index)
                            selectedIdx = when {
                                index < selectedIdx -> selectedIdx - 1
                                index == selectedIdx -> -1
                                else -> selectedIdx
                            }
                            itemsList = current
                            callback?.del(item, current)
                        }
                    )
                }
            }
        }
        setContentView(composeView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setTip(tip: String?) {
        titleText = tip ?: "配置历史"
        setTitle(tip)
    }

    fun setAdapter(
        sourceBeanSelectDialogInterface: ApiHistoryDialogAdapter.SelectDialogInterface?,
        data: List<String>?,
        select: Int
    ) {
        this.callback = sourceBeanSelectDialogInterface
        this.itemsList = data ?: emptyList()
        this.selectedIdx = select
    }
}
