package com.github.tvbox.osc.ui.dialog

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.bean.MovieSort
import com.github.tvbox.osc.ui.compose.GridFilterDialogContent

class GridFilterDialog(context: Context) : BaseDialog(context) {

    private var sortDataState by mutableStateOf<MovieSort.SortData?>(null)
    private var selectChange = false
    private var onChanged: Callback? = null

    init {
        sheetLayoutName = "dialog_grid_filter"
        setCanceledOnTouchOutside(false)
        setCancelable(true)

        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    sortDataState?.let { data ->
                        GridFilterDialogContent(
                            sortData = data,
                            onFilterSelected = { key, value, deselect ->
                                if (deselect) {
                                    data.filterSelect?.remove(key)
                                } else {
                                    data.filterSelect?.put(key, value)
                                }
                                selectChange = true
                                onChanged?.change()
                                // trigger recomposition
                                sortDataState = MovieSort.SortData().apply {
                                    filters = data.filters
                                    filterSelect = java.util.HashMap(data.filterSelect ?: emptyMap())
                                }
                            }
                        )
                    }
                }
            }
        }
        setContentView(composeView)
    }

    fun interface Callback {
        fun change()
    }

    fun setOnDismiss(callback: Callback) {
        setOnDismissListener {
            if (selectChange) {
                callback.change()
            }
        }
    }

    fun setOnChanged(callback: Callback?) {
        onChanged = callback
    }

    fun setData(sortData: MovieSort.SortData) {
        this.sortDataState = sortData
    }

    override fun show() {
        selectChange = false
        super.show()
    }
}
