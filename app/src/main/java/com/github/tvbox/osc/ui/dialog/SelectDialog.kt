package com.github.tvbox.osc.ui.dialog

import android.content.Context
import android.os.Bundle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.DiffUtil
import com.github.tvbox.osc.ui.adapter.SelectDialogAdapter
import com.github.tvbox.osc.ui.compose.SelectDialogContent

class SelectDialog<T : Any> : BaseDialog {

    private var itemsList by mutableStateOf<List<T>>(emptyList())
    private var selectedIdx by mutableStateOf(0)
    private var titleText by mutableStateOf<String?>(null)
    private var selectInterface: SelectDialogAdapter.SelectDialogInterface<T>? = null

    constructor(context: Context) : super(context) {
        initDialog()
    }

    constructor(context: Context, resId: Int) : super(context) {
        initDialog()
    }

    private fun initDialog() {
        sheetLayoutName = "dialog_select"
        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    SelectDialogContent(
                        items = itemsList,
                        selectedIndex = selectedIdx,
                        itemLabel = { item ->
                            selectInterface?.getDisplay(item) ?: item.toString()
                        },
                        onItemSelected = { item, index ->
                            selectedIdx = index
                            selectInterface?.click(item, index)
                            dismiss()
                        },
                        title = titleText
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
        titleText = tip
        setTitle(tip)
    }

    fun setAdapter(
        sourceBeanSelectDialogInterface: SelectDialogAdapter.SelectDialogInterface<T>?,
        sourceBeanItemCallback: DiffUtil.ItemCallback<T>?,
        data: List<T>?,
        select: Int
    ) {
        this.selectInterface = sourceBeanSelectDialogInterface
        this.itemsList = data ?: emptyList()
        this.selectedIdx = select
    }
}
