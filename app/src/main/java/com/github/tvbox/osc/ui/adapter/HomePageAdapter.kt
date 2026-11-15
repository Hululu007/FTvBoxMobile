package com.github.tvbox.osc.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.github.tvbox.osc.base.ScreenSection

class HomePageAdapter : PagerAdapter {
    @JvmField
    var list: MutableList<ScreenSection>? = null
    private val adapterId = System.nanoTime()

    constructor() : super()

    constructor(fm: Any?) : super()

    constructor(fm: Any?, list: MutableList<ScreenSection>?) : super() {
        this.list = list
    }

    constructor(list: MutableList<ScreenSection>?) : super() {
        this.list = list
    }

    fun clear() {
        list?.clear()
        notifyDataSetChanged()
    }

    fun removeAll() {
        val currentList = list ?: return
        for (item in currentList) {
            item.destroy()
        }
        currentList.clear()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): ScreenSection? {
        return list?.getOrNull(position)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    fun getItemId(position: Int): Long {
        return adapterId + position
    }

    override fun getCount(): Int {
        return list?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        val item = `object` as? ScreenSection
        return item?.getRootView() == view || view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = list!![position]
        item.setUserVisibleHint(false)
        val view = item.getRootView() ?: item.createView(container.context, container)
        if (view.parent != null && view.parent !== container) {
            (view.parent as? ViewGroup)?.removeView(view)
        }
        if (view.parent == null) {
            container.addView(view)
        }
        return item
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        list?.forEach { it.setUserVisibleHint(it === `object`) }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val item = `object` as? ScreenSection
        item?.onHiddenChanged(true)
        val view = item?.getRootView()
        if (view != null && view.parent === container) {
            container.removeView(view)
        }
    }
}
