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
import com.github.tvbox.osc.ui.compose.SelectDialogItem
import java.util.ArrayList

// Callers can pass value types; the fallback uses their normal equality semantics.
@SuppressLint("DiffUtilEquals")
class SelectDialogAdapter<T : Any>(
    private val dialogInterface: SelectDialogInterface<T>?,
    diffCallback: DiffUtil.ItemCallback<T>?
) : ListAdapter<T, SelectDialogAdapter.SelectViewHolder>(
    diffCallback ?: object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    }
) {

    class SelectViewHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    interface SelectDialogInterface<T> {
        fun click(value: T, pos: Int)
        fun getDisplay(valItem: T): String
    }

    companion object {
        @JvmField
        val stringDiff = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    private val data = ArrayList<T>()
    private var select = 0

    fun setData(newData: List<T>?, defaultSelect: Int) {
        data.clear()
        if (newData != null) {
            data.addAll(newData)
        }
        select = defaultSelect
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

    override fun onBindViewHolder(holder: SelectViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val value = data[position]
        val name = dialogInterface?.getDisplay(value) ?: ""
        val isSelected = position == select

        holder.composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                SelectDialogItem(
                    text = name,
                    isSelected = isSelected,
                    onClick = {
                        if (position == select) return@SelectDialogItem
                        val oldSelect = select
                        select = position
                        notifyItemChanged(oldSelect)
                        notifyItemChanged(select)
                        dialogInterface?.click(value, position)
                    }
                )
            }
        }
    }
}
