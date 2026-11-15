package com.github.tvbox.osc.util

import com.github.tvbox.osc.bean.VodInfo
import java.util.Locale

/** Deduplicates and orders episodes within one route, preserving distinct versions and extras. */
object EpisodeOrder {
    data class Key(val version: String, val season: Int, val episode: Int)

    private val special = Regex("预告|花絮|片花|特辑|番外|彩蛋|特别篇|总集篇|trailer|preview|special|behind")
    private val quality = Regex("(?i)2160p|1080p|720p|480p|4k|h26[45]|x26[45]|mp4")
    private val seasonEpisode = Regex("(?i)\\bs(\\d{1,3})[\\s._-]*e(\\d{1,4})\\b")
    private val season = Regex("第\\s*([0-9零〇一二两三四五六七八九十百千]+)\\s*季")
    private val episode = Regex("第\\s*([0-9零〇一二两三四五六七八九十百千]+)\\s*[集话期]|\\b(?:episode|ep|e)\\s*(\\d{1,5})\\b", RegexOption.IGNORE_CASE)
    private val digits = Regex("\\d+")
    private val separators = Regex("[\\s\\p{P}\\p{S}]+")

    fun key(name: String?): Key? {
        if (name.isNullOrBlank()) return null
        var text = name.lowercase(Locale.ROOT)
        if (special.containsMatchIn(text)) return null
        text = quality.replace(text, "")
        var seasonNumber = 0
        val combined = seasonEpisode.find(text)
        val match = if (combined != null) {
            seasonNumber = combined.groupValues[1].toInt()
            combined
        } else {
            season.find(text)?.let {
                seasonNumber = number(it.groupValues[1]) ?: return null
                text = text.removeRange(it.range)
            }
            episode.find(text) ?: digits.findAll(text).toList().singleOrNull() ?: return null
        }
        val episodeText = if (combined != null) combined.groupValues[2]
            else match.groupValues.drop(1).firstOrNull { it.isNotEmpty() } ?: match.value
        val episodeNumber = number(episodeText)?.takeIf { it in 0..99999 } ?: return null
        val version = separators.replace(text.removeRange(match.range), "")
        return Key(version, seasonNumber, episodeNumber)
    }

    fun normalize(list: MutableList<VodInfo.VodSeries>) {
        val episodes = hashSetOf<Key>()
        val unnumbered = hashSetOf<Pair<String?, String?>>()
        val iterator = list.iterator()
        while (iterator.hasNext()) {
            val series = iterator.next()
            val key = key(series.name)
            // Keep the first name/URL pair for an episode, even when its alias has another URL.
            val first = if (key != null) episodes.add(key) else unnumbered.add(series.name to series.url)
            if (!first) iterator.remove()
        }
        val groups = linkedMapOf<String, MutableList<Pair<Int, Key>>>()
        list.forEachIndexed { index, series ->
            key(series.name)?.let { groups.getOrPut(it.version) { arrayListOf() }.add(index to it) }
        }
        for (entries in groups.values) {
            val ordered = entries.sortedWith(compareBy({ it.second.season }, { it.second.episode }))
                .map { list[it.first] }
            entries.forEachIndexed { index, entry -> list[entry.first] = ordered[index] }
        }
    }

    private fun number(value: String): Int? {
        value.toIntOrNull()?.let { return it }
        var result = 0
        var digit = 0
        for (character in value) {
            val unit = when (character) { '十' -> 10; '百' -> 100; '千' -> 1000; else -> 0 }
            if (unit > 0) {
                result += (if (digit == 0) 1 else digit) * unit
                digit = 0
            } else {
                digit = when (character) {
                    '零', '〇' -> 0; '一' -> 1; '二', '两' -> 2; '三' -> 3; '四' -> 4
                    '五' -> 5; '六' -> 6; '七' -> 7; '八' -> 8; '九' -> 9
                    else -> return null
                }
            }
        }
        return (result + digit).takeIf { value.isNotEmpty() }
    }
}
