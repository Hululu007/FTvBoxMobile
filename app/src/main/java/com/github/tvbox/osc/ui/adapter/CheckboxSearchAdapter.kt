package com.github.tvbox.osc.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.tvbox.osc.bean.SourceBean
import com.github.tvbox.osc.ui.compose.DialogCheckboxSearchItem
import com.github.tvbox.osc.util.SearchHelper
import java.util.ArrayList
import java.util.HashMap

class CheckboxSearchAdapter(diffCallback: DiffUtil.ItemCallback<SourceBean>) :
    ListAdapter<SourceBean, CheckboxSearchAdapter.ViewHolder>(diffCallback) {

    private val data = ArrayList<SourceBean>()
    @JvmField
    var mCheckedSources: HashMap<String, String>? = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return ViewHolder(composeView)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<SourceBean>, checkedSources: HashMap<String, String>?) {
        data.clear()
        data.addAll(newData)
        mCheckedSources = checkedSources
        notifyDataSetChanged()
    }

    fun setMCheckedSources() {
        val checked = mCheckedSources ?: HashMap()
        SearchHelper.putCheckedSources(checked, data.size == checked.size)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = holder.adapterPosition
        if (pos !in data.indices) return
        val sourceBean = data[pos]
        val checked = mCheckedSources
        val isChecked = checked?.containsKey(sourceBean.key) == true

        holder.composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                DialogCheckboxSearchItem(
                    name = sourceBean.name ?: "",
                    isChecked = isChecked,
                    onCheckedChange = { checkState ->
                        val curChecked = mCheckedSources ?: HashMap<String, String>().also { mCheckedSources = it }
                        val key = sourceBean.key
                        if (key != null) {
                            if (checkState) {
                                curChecked[key] = "1"
                            } else {
                                curChecked.remove(key)
                            }
                        }
                        notifyItemChanged(pos)
                    }
                )
            }
        }
    }

    class ViewHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)
}
