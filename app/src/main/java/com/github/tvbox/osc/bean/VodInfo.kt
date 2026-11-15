package com.github.tvbox.osc.bean

import com.github.tvbox.osc.util.EpisodeOrder
import java.io.Serializable
import java.util.ArrayList
import java.util.Collections
import java.util.LinkedHashMap

/**
 * @author pj567
 * @date :2020/12/22
 * @description:
 */
class VodInfo : Serializable {
    @JvmField
    var last: String? = null // 时间
    @JvmField
    var id: String? = null // 内容id
    @JvmField
    var tid: Int = 0 // 父级id
    @JvmField
    var name: String? = null // 影片名称 <![CDATA[老爸当家]]>
    @JvmField
    var type: String? = null // 类型名称
    @JvmField
    var dt: String? = null // 视频分类zuidam3u8,zuidall
    @JvmField
    var pic: String? = null // 图片
    @JvmField
    var lang: String? = null // 语言
    @JvmField
    var area: String? = null // 地区
    @JvmField
    var year: Int = 0 // 年份
    @JvmField
    var state: String? = null
    @JvmField
    var note: String? = null // 描述集数或者影片信息<![CDATA[共40集]]>
    @JvmField
    var actor: String? = null // 演员<![CDATA[张国立,蒋欣,高鑫,曹艳艳,王维维,韩丹彤,孟秀,王新]]>
    @JvmField
    var director: String? = null // 导演<![CDATA[陈国星]]>
    @JvmField
    var seriesFlags: ArrayList<VodSeriesFlag>? = null
    @JvmField
    var seriesMap: LinkedHashMap<String, MutableList<VodSeries>>? = null
    @JvmField
    var des: String? = null // <![CDATA[权来]
    @JvmField
    var playFlag: String? = null
    @JvmField
    var playIndex: Int = 0
    @JvmField
    var playNote: String = ""
    @JvmField
    var sourceKey: String? = null
    @JvmField
    var playerCfg: String = ""
    @JvmField
    var reverseSort: Boolean = false

    fun setVideo(video: Movie.Video) {
        last = video.last
        id = video.id
        tid = video.tid
        name = video.name
        type = video.type
        pic = video.pic
        lang = video.lang
        area = video.area
        year = video.year
        state = video.state
        note = video.note
        actor = video.actor
        director = video.director
        des = video.des
        if (video.urlBean != null && video.urlBean?.infoList != null && video.urlBean!!.infoList!!.isNotEmpty()) {
            val tempSeriesMap = LinkedHashMap<String, MutableList<VodSeries>>()
            val flags = ArrayList<VodSeriesFlag>()
            for (urlInfo in video.urlBean!!.infoList!!) {
                if (urlInfo.beanList != null && urlInfo.beanList!!.isNotEmpty()) {
                    val seriesList = ArrayList<VodSeries>()
                    for (infoBean in urlInfo.beanList!!) {
                        seriesList.add(VodSeries(infoBean.name, infoBean.url))
                    }
                    tempSeriesMap[urlInfo.flag ?: ""] = seriesList
                    flags.add(VodSeriesFlag(urlInfo.flag))
                }
            }

            seriesFlags = flags
            seriesMap = LinkedHashMap()
            for (flag in flags) {
                val list = tempSeriesMap[flag.name ?: ""]
                if (list != null) {
                    EpisodeOrder.normalize(list)
                    seriesMap!![flag.name ?: ""] = list
                }
            }
        }
    }

    fun restorePlayback(record: VodInfo?) {
        val reversed = record?.reverseSort ?: false
        if (reverseSort != reversed) reverse()
        reverseSort = reversed
        val map = seriesMap
        playFlag = record?.playFlag?.takeIf { map?.containsKey(it) == true } ?: map?.keys?.firstOrNull()
        playerCfg = record?.playerCfg.orEmpty()
        playNote = record?.playNote.orEmpty()
        val list = map?.get(playFlag).orEmpty()
        val exact = if (playNote.isNotBlank()) list.indexOfFirst { it.name == playNote } else -1
        val savedKey = EpisodeOrder.key(playNote)
        val matched = if (exact >= 0) exact else if (savedKey != null) {
            list.indexOfFirst { EpisodeOrder.key(it.name) == savedKey }
        } else -1
        // Old records store an index and a display name, not the episode list.
        // Match the name first so normalizing a source's order does not resume another video.
        playIndex = (if (matched >= 0) matched else record?.playIndex ?: 0)
            .coerceIn(0, maxOf(0, list.size - 1))
    }

    fun reverse() {
        val sMap = seriesMap ?: return
        for (flag in sMap.keys) {
            val list = sMap[flag]
            if (list != null) Collections.reverse(list)
        }
    }

    class VodSeriesFlag : Serializable {
        @JvmField
        var name: String? = null
        @JvmField
        var selected: Boolean = false

        constructor()

        constructor(name: String?) {
            this.name = name
        }
    }

    class VodSeries : Serializable {
        @JvmField
        var name: String? = null
        @JvmField
        var url: String? = null
        @JvmField
        var selected: Boolean = false

        constructor()

        constructor(name: String?, url: String?) {
            this.name = name
            this.url = url
        }
    }
}
