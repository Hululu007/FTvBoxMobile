package com.github.tvbox.osc.ui.adapter

import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.tvbox.osc.ui.compose.DialogApiHistoryItem
import com.github.tvbox.osc.util.HistoryHelper
import java.util.ArrayList

class ApiHistoryDialogAdapter(private val dialogInterface: SelectDialogInterface?) :
    ListAdapter<String, ApiHistoryDialogAdapter.SelectViewHolder>(object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }) {

    class SelectViewHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    interface SelectDialogInterface {
        fun click(value: String)
        fun del(value: String, data: ArrayList<String>)
    }

    private val data = ArrayList<String>()
    private var select = ""

    fun setData(newData: List<String>?, defaultSelect: Int) {
        data.clear()
        if (newData != null) {
            data.addAll(newData)
        }
        select = if (defaultSelect in data.indices) data[defaultSelect] else ""
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return SelectViewHolder(composeView)
    }

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        val value = data[position]
        var name = value
        if (HistoryHelper.isApiLineSource(value)) {
            name = "[仓] $name"
        }
        if (select == value) {
            name = "√ $name"
        }

        holder.composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                DialogApiHistoryItem(
                    name = name,
                    onNameClick = {
                        if (select == value) return@DialogApiHistoryItem
                        val oldIndex = data.indexOf(select)
                        select = value
                        if (oldIndex != -1) notifyItemChanged(oldIndex)
                        val newIndex = data.indexOf(value)
                        if (newIndex != -1) notifyItemChanged(newIndex)
                        dialogInterface?.click(value)
                    },
                    onDeleteClick = {
                        if (select == value) return@DialogApiHistoryItem
                        val removeIndex = data.indexOf(value)
                        if (removeIndex != -1) {
                            notifyItemRemoved(removeIndex)
                            data.remove(value)
                            dialogInterface?.del(value, data)
                        }
                    }
                )
            }
        }
    }
}
