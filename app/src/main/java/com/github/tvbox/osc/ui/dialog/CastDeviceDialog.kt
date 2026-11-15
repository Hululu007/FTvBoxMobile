package com.github.tvbox.osc.ui.dialog

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.tvbox.osc.dlna.CastDevice
import com.github.tvbox.osc.dlna.CastVideo
import com.github.tvbox.osc.dlna.DLNACastManager
import com.github.tvbox.osc.player.thirdparty.RemoteTVBox
import com.github.tvbox.osc.ui.compose.CastDialogContent
import okhttp3.Call
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashMap

class CastDeviceDialog(context: Context, private val video: CastVideo) : BaseDialog(context) {
    private val handler = Handler(Looper.getMainLooper())
    private val devices: MutableMap<String, CastDevice> = LinkedHashMap()
    private var onCastListener: OnCastListener? = null
    private var searchFinished by mutableStateOf(false)
    private var deviceList by mutableStateOf<List<CastDevice>>(emptyList())

    init {
        sheetLayoutName = "dialog_cast"
        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    CastDialogContent(
                        devices = deviceList,
                        isSearching = !searchFinished && deviceList.isEmpty(),
                        onDeviceClick = { device -> castToDevice(device) },
                        onRefresh = { refreshDevices() },
                        onCancel = { dismiss() }
                    )
                }
            }
        }
        setContentView(composeView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchDevices()
    }

    override fun dismiss() {
        DLNACastManager.get().setDeviceListener(null)
        DLNACastManager.get().release(context)
        super.dismiss()
    }

    fun setOnCastListener(listener: OnCastListener?) {
        this.onCastListener = listener
    }

    private fun searchDevices() {
        searchFinished = false
        searchTvBoxDevices()
        searchDlnaDevices()
        handler.postDelayed({
            searchFinished = true
        }, 15000)
    }

    private fun refreshDevices() {
        handler.removeCallbacksAndMessages(null)
        DLNACastManager.get().setDeviceListener(null)
        DLNACastManager.get().release(context)
        devices.clear()
        deviceList = emptyList()
        searchDevices()
    }

    private fun searchTvBoxDevices() {
        Thread {
            RemoteTVBox.searchAvalible(object : RemoteTVBox.Callback() {
                override fun found(viewHost: String, end: Boolean) {
                    handler.post {
                        addDevice(CastDevice.tvbox(viewHost))
                    }
                }

                override fun fail(all: Boolean, end: Boolean) {
                    if (end) {
                        handler.post {
                            // finished
                        }
                    }
                }
            })
        }.start()
    }

    private fun searchDlnaDevices() {
        DLNACastManager.get().setDeviceListener(object : DLNACastManager.DeviceListener {
            override fun onDeviceChanged() {
                for (device in DLNACastManager.get().getDevices()) {
                    addDevice(device)
                }
            }
        })
        DLNACastManager.get().init(context)
        handler.postDelayed({
            DLNACastManager.get().search()
        }, 1000)
    }

    private fun addDevice(device: CastDevice) {
        devices[device.type.toString() + ":" + device.id] = device
        deviceList = ArrayList(devices.values)
    }

    private fun castToDevice(device: CastDevice) {
        if (device.type == CastDevice.TYPE_TVBOX) {
            castToTvBox(device)
            return
        }
        DLNACastManager.get().cast(device, video, object : DLNACastManager.CastCallback {
            override fun onResult(success: Boolean, msg: String) {
                handleCastResult(success, msg)
            }
        })
    }

    private fun castToTvBox(device: CastDevice) {
        try {
            val url = buildTvBoxUrl(video.url, video.headers)
            val params: MutableMap<String, String> = HashMap()
            params["do"] = "push"
            params["url"] = url
            RemoteTVBox.post("http://" + device.id + "/action", params, object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    handler.post {
                        handleCastResult(false, "TVBox投屏失败")
                    }
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val ok: Boolean
                    try {
                        val body = response.body()
                        ok = body != null && "ok" == body.string()
                    } finally {
                        response.close()
                    }
                    handler.post {
                        handleCastResult(ok, if (ok) "" else "TVBox投屏失败")
                    }
                }
            })
        } catch (e: Exception) {
            handleCastResult(false, "TVBox投屏失败")
        }
    }

    @Throws(Exception::class)
    private fun buildTvBoxUrl(url: String, headers: HashMap<String, String>?): String {
        if (headers == null || headers.isEmpty()) return url
        val json = JSONObject(headers as Map<*, *>).toString()
        return url + "@Headers=" + URLEncoder.encode(json, "UTF-8") + "@"
    }

    private fun handleCastResult(success: Boolean, msg: String?) {
        if (success) {
            Toast.makeText(context, "投屏成功", Toast.LENGTH_SHORT).show()
            onCastListener?.onCastSuccess()
            dismiss()
        } else {
            Toast.makeText(context, if (msg.isNullOrEmpty()) "投屏失败" else msg, Toast.LENGTH_SHORT).show()
            onCastListener?.onCastFailed()
        }
    }

    interface OnCastListener {
        fun onCastSuccess()
        fun onCastFailed()
    }
}
