package com.github.tvbox.osc.ui.dialog

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.ComponentDialog
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.fbox.mobile.MobileSheets
import com.github.tvbox.osc.R
import xyz.doikki.videoplayer.util.CutoutUtil

open class BaseDialog : ComponentDialog {
    private var phoneSheetInstalled = false
    private var sheetLayout = ""
    var sheetLayoutName: String
        get() = sheetLayout
        set(value) {
            sheetLayout = value
        }
    @JvmField
    var sheetTitle: CharSequence? = null

    override fun setContentView(layoutResID: Int) {
        sheetLayout = context.resources.getResourceEntryName(layoutResID)
        super.setContentView(layoutResID)
    }

    override fun setContentView(view: View) {
        // Keep owners on the content while MobileSheets reparents it and on dialog reuse.
        view.setViewTreeLifecycleOwner(this)
        view.setViewTreeSavedStateRegistryOwner(this)
        super.setContentView(view)
    }

    override fun setTitle(title: CharSequence?) {
        sheetTitle = title
        super.setTitle(title)
    }

    open fun getSheetTitle(): CharSequence {
        val st = sheetTitle
        if (st != null) return st
        val titleId = context.resources.getIdentifier("title", "id", context.packageName)
        val epTitleId = context.resources.getIdentifier("episode_title", "id", context.packageName)
        for (id in intArrayOf(titleId, epTitleId)) {
            if (id != 0) {
                val label = findViewById<TextView>(id)
                if (label != null && label.text.isNotEmpty()) {
                    label.visibility = View.GONE
                    return label.text
                }
            }
        }
        return when (sheetLayout) {
            "dialog_api" -> "订阅配置"
            "dialog_api_history" -> "配置历史"
            "dialog_backup" -> "备份与恢复"
            "dialog_cast" -> "选择投屏设备"
            "dialog_checkbox_search" -> "搜索范围"
            "dialog_confirm" -> "清空记录"
            "dialog_danmu_api" -> "弹幕地址"
            "dialog_danmu_setting" -> "弹幕设置"
            "dialog_desc" -> "影片资料"
            "dialog_episode" -> "选集"
            "dialog_grid_filter" -> "筛选"
            "dialog_live_password" -> "频道密码"
            "dialog_quick_search" -> "快速搜索"
            "dialog_remote" -> "远程连接"
            "dialog_search_danmu" -> "搜索弹幕"
            "dialog_search_remotetv" -> "搜索设备"
            "dialog_search_subtitle" -> "搜索字幕"
            "dialog_subtitle" -> "字幕设置"
            "dialog_about" -> "关于 FBox"
            "dialog_xwalk" -> "安装播放组件"
            "dialog_tip" -> "提示"
            else -> "请选择"
        }
    }

    constructor(context: Context) : super(context, R.style.CustomDialogStyle)

    constructor(context: Context, customDialogStyle: Int) : super(context, customDialogStyle)

    override fun onCreate(savedInstanceState: Bundle?) {
        CutoutUtil.adaptCutoutAboveAndroidP(this, true)
        super.onCreate(savedInstanceState)
    }

    override fun show() {
        if (isContextInvalid()) {
            return
        }
        initializeViewTreeOwners()
        window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        super.show()
        hideSysBar()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        if (!phoneSheetInstalled) {
            com.fbox.mobile.MobileActions.installDialog(findViewById(android.R.id.content)!!, sheetLayout)
            MobileSheets.install(this)
            phoneSheetInstalled = true
        }
        val metrics = context.resources.displayMetrics
        val portrait = metrics.heightPixels > metrics.widthPixels
        val win = window
        if (win != null) {
            if (portrait) {
                win.setGravity(Gravity.BOTTOM)
                win.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                win.setWindowAnimations(R.style.MobileSheetBottomAnimation)
            } else {
                win.setGravity(Gravity.END or Gravity.TOP)
                val panelWidth = Math.min(metrics.widthPixels, MobileSheets.dp(context, 380f))
                win.setLayout(
                    panelWidth,
                    WindowManager.LayoutParams.MATCH_PARENT
                )
                win.setWindowAnimations(R.style.MobileSheetRightAnimation)
            }
            win.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            win.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    private fun isContextInvalid(): Boolean {
        val ctx = context
        if (ctx !is Activity) {
            return false
        }
        return ctx.isFinishing || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ctx.isDestroyed)
    }

    private fun hideSysBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            var uiOptions = window?.decorView?.systemUiVisibility ?: 0
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_FULLSCREEN
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window?.decorView?.systemUiVisibility = uiOptions
        }
    }
}
