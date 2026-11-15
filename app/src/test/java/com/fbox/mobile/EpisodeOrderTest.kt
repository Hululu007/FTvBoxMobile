package com.fbox.mobile

import com.github.tvbox.osc.bean.Movie
import com.github.tvbox.osc.bean.VodInfo
import org.junit.Assert.*
import org.junit.Test

class EpisodeOrderTest {
    @Test
    fun mixedPrefixAndSuffixEpisodeNumbersSortOnEveryRoute() {
        val names = listOf("普通话版(10)", "普通话版(11)", "1(普通话版)", "2(普通话版)", "9(普通话版)")
        val info = video(names, routes = 6)
        for (list in info.seriesMap!!.values) {
            assertEquals(listOf(names[2], names[3], names[4], names[0], names[1]), list.map { it.name })
            assertEquals(listOf("url-2", "url-3", "url-4", "url-0", "url-1"), list.map { it.url })
        }
    }

    @Test
    fun languageVersionsAndExtrasKeepTheirPlaces() {
        val names = listOf("国语第10集", "花絮1", "2(国语)", "国语(1)", "粤语第2集", "1(粤语)", "预告片2")
        val list = video(names).seriesMap!!["route-0"]!!
        assertEquals(listOf("国语(1)", "花絮1", "2(国语)", "国语第10集", "1(粤语)", "粤语第2集", "预告片2"), list.map { it.name })
    }

    @Test
    fun seasonsAndChineseEpisodeNumbersDoNotUseResolutionAsEpisode() {
        val names = listOf("S02E01", "S01E10", "S01E02")
        assertEquals(listOf("S01E02", "S01E10", "S02E01"), video(names).seriesMap!!["route-0"]!!.map { it.name })
        val chinese = listOf("1080P 第十集", "1080P 第二集", "1080P 第九集")
        assertEquals(listOf(chinese[1], chinese[2], chinese[0]), video(chinese).seriesMap!!["route-0"]!!.map { it.name })
    }

    @Test
    fun unknownDatesAndOversizedNumbersRemainInSourceOrder() {
        val names = listOf("2024-12-20", "1080P", "2024-12-01", "999999999999999999999999", "正片")
        assertEquals(names, video(names).seriesMap!!["route-0"]!!.map { it.name })
    }

    @Test
    fun historyFollowsEpisodeNameAfterNormalizationAndReverse() {
        val info = video(listOf("普通话版(10)", "普通话版(11)", "1(普通话版)", "2(普通话版)"))
        val record = VodInfo().apply {
            playFlag = "route-0"
            playIndex = 2 // Position before the source order was corrected.
            playNote = "1(普通话版)"
            playerCfg = "{\"st\":90}"
        }
        info.restorePlayback(record)
        assertEquals(0, info.playIndex)
        assertEquals("url-2", info.seriesMap!![info.playFlag]!![info.playIndex].url)
        assertEquals(record.playerCfg, info.playerCfg)

        record.reverseSort = true
        record.playNote = "普通话版(1)" // Same episode with another supported label format.
        info.restorePlayback(record)
        assertEquals(3, info.playIndex)
        assertEquals("url-2", info.seriesMap!![info.playFlag]!![info.playIndex].url)
        assertEquals(listOf("普通话版(11)", "普通话版(10)", "2(普通话版)", "1(普通话版)"),
            info.seriesMap!![info.playFlag]!!.map { it.name })
    }

    @Test
    fun repeatedAliasesKeepOneEpisodeAndItsOriginalUrlOnEveryRoute() {
        val names = listOf("普通话版(10)", "1(普通话版)", "10(普通话版)", "普通话版(1)", "2(普通话版)", "2(普通话版)")
        for (list in video(names, routes = 6).seriesMap!!.values) {
            assertEquals(listOf("1(普通话版)", "2(普通话版)", "普通话版(10)"), list.map { it.name })
            assertEquals(listOf("url-1", "url-4", "url-0"), list.map { it.url })
        }
    }

    @Test
    fun deduplicationPreservesLanguageSeasonAndParts() {
        val names = listOf("国语第1集", "粤语第1集", "第一季第1集", "第二季第1集",
            "第1集上", "第1集下", "1(国语)", "第一季第一集", "第1集（上）")
        val list = video(names).seriesMap!!["route-0"]!!
        assertEquals(names.take(6), list.map { it.name })
    }

    @Test
    fun historyOfRemovedAliasStillResumesTheRetainedEpisode() {
        val info = video(listOf("普通话版(10)", "1(普通话版)", "10(普通话版)", "2(普通话版)"))
        val record = VodInfo().apply {
            playFlag = "route-0"
            playIndex = 2
            playNote = "10(普通话版)"
        }
        info.restorePlayback(record)
        assertEquals(2, info.playIndex)
        assertEquals("url-0", info.seriesMap!![info.playFlag]!![info.playIndex].url)
        record.reverseSort = true
        info.restorePlayback(record)
        assertEquals(0, info.playIndex)
        assertEquals("url-0", info.seriesMap!![info.playFlag]!![info.playIndex].url)
    }

    private fun video(names: List<String>, routes: Int = 1): VodInfo {
        val video = Movie.Video().apply {
            urlBean = Movie.Video.UrlBean().apply {
                infoList = (0 until routes).map { route ->
                    Movie.Video.UrlBean.UrlInfo().apply {
                        flag = "route-$route"
                        beanList = names.mapIndexed { index, name ->
                            Movie.Video.UrlBean.UrlInfo.InfoBean(name, "url-$index")
                        }.toMutableList()
                    }
                }.toMutableList()
            }
        }
        return VodInfo().apply { setVideo(video) }
    }
}
