package com.github.tvbox.osc.ui.dialog

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.DiffUtil
import com.github.tvbox.osc.event.RefreshEvent
import com.github.tvbox.osc.player.thirdparty.RemoteTVBox
import com.github.tvbox.osc.ui.adapter.SelectDialogAdapter
import com.github.tvbox.osc.ui.compose.SearchRemoteTvDialogContent
import com.github.tvbox.osc.ui.fragment.ModelSettingFragment
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SearchRemoteTvDialog(context: Context) : BaseDialog(context) {

    private var titleText by mutableStateOf("")
    private var isLoadingState by mutableStateOf(true)

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refresh(event: RefreshEvent) {
        if (event.type == RefreshEvent.TYPE_SETTING_SEARCH_TV) {
            showRemoteTvDialog(ModelSettingFragment.foundRemoteTv)
        }
    }

    init {
        sheetLayoutName = "dialog_search_remotetv"
        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    SearchRemoteTvDialogContent(
                        title = titleText,
                        isLoading = isLoadingState
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
        titleText = tip ?: ""
        setTitle(tip)
        showLoading()
    }

    private fun showRemoteTvDialog(found: Boolean) {
        if (!found) {
            ModelSettingFragment.loadingSearchRemoteTvDialog?.showEmpty()
            Toast.makeText(context, "未找到附近TVBox", Toast.LENGTH_SHORT).show()
            return
        }
        ModelSettingFragment.loadingSearchRemoteTvDialog?.dismiss()
        val hosts = ModelSettingFragment.remoteTvHostList ?: return
        if (hosts.isNotEmpty()) {
            RemoteTVBox.setAvalible(hosts[0])
        }
        val dialog = SelectDialog<String>(context)
        dialog.setTip("附近TVBox")
        val defaultPos = 0
        dialog.setAdapter(object : SelectDialogAdapter.SelectDialogInterface<String> {
            override fun click(value: String, pos: Int) {
                RemoteTVBox.setAvalible(value)
                Toast.makeText(context, "设置成功", Toast.LENGTH_SHORT).show()
            }

            override fun getDisplay(valStr: String): String {
                return valStr
            }
        }, object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }, hosts, defaultPos)
        dialog.show()
    }

    fun showLoading() {
        isLoadingState = true
    }

    fun showEmpty() {
        isLoadingState = false
    }

    fun showSuccess() {
        isLoadingState = false
    }
}
