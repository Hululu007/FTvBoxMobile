package com.github.tvbox.osc.ui.dialog

import android.app.Activity
import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.bean.SourceBean
import com.github.tvbox.osc.ui.compose.CheckboxSearchDialogContent
import java.util.HashMap

class SearchCheckboxDialog(
    context: Context,
    @JvmField var mSourceList: MutableList<SourceBean>,
    @JvmField var mCheckSourcees: HashMap<String, String>?
) : BaseDialog(context) {

    private var sourceListState by mutableStateOf<List<SourceBean>>(emptyList())
    private var checkedSourcesState by mutableStateOf<Map<String, String>>(emptyMap())

    init {
        sheetLayoutName = "dialog_checkbox_search"
        if (context is Activity) {
            setOwnerActivity(context)
        }
        setCanceledOnTouchOutside(false)
        setCancelable(true)

        sourceListState = mSourceList
        checkedSourcesState = mCheckSourcees ?: HashMap()

        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    CheckboxSearchDialogContent(
                        sourceList = sourceListState,
                        checkedSources = checkedSourcesState,
                        onSourceToggle = { source, checked ->
                            val map = HashMap(checkedSourcesState)
                            if (checked) {
                                map[source.key] = "1"
                            } else {
                                map.remove(source.key)
                            }
                            checkedSourcesState = map
                            mCheckSourcees = map
                        },
                        onCheckAll = {
                            val map = HashMap<String, String>()
                            for (source in sourceListState) {
                                map[source.key] = "1"
                            }
                            checkedSourcesState = map
                            mCheckSourcees = map
                        },
                        onClearAll = {
                            val map = HashMap<String, String>()
                            checkedSourcesState = map
                            mCheckSourcees = map
                        }
                    )
                }
            }
        }
        setContentView(composeView)
    }

    override fun dismiss() {
        super.dismiss()
    }

    fun setMSourceList(sourceBeanList: MutableList<SourceBean>) {
        mSourceList = sourceBeanList
        sourceListState = sourceBeanList
    }
}
