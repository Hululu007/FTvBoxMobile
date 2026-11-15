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
import com.github.tvbox.osc.ui.compose.SelectButtonItem
import java.util.ArrayList

class ButtonAdapter<T : Any>(
    private val dialogInterface: SelectDialogInterface<T>,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, ButtonAdapter.SelectViewHolder>(diffCallback) {

    interface SelectDialogInterface<T> {
        fun click(value: T, pos: Int)
        fun getDisplay(value: T): String
    }

    class SelectViewHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    private val data = ArrayList<T>()
    private var select = 0

    fun setData(newData: List<T>, defaultSelect: Int) {
        data.clear()
        data.addAll(newData)
        select = Math.max(0, Math.min(defaultSelect, data.size - 1))
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
        val text = dialogInterface.getDisplay(value)
        val isSelected = position == select

        holder.composeView.setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFAEC6FF),
                    background = Color(0xFF10131A),
                    surface = Color(0xFF171C26),
                )
            ) {
                SelectButtonItem(
                    text = text,
                    isSelected = isSelected,
                    onClick = {
                        if (position == select) return@SelectButtonItem
                        val oldSelect = select
                        select = position
                        notifyItemChanged(oldSelect)
                        notifyItemChanged(select)
                        dialogInterface.click(value, position)
                    }
                )
            }
        }
    }
}
