package com.example.flux.model.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

interface TimeDelegate {

    private fun zoneId() = ZoneId.of("Asia/Tokyo")

    private fun getFormatterForApi(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            .withZone(zoneId())
    }

    fun parseForApi(date: String): LocalDateTime {
        return try {
            LocalDateTime.parse(date, getFormatterForApi())
        } catch (e: Exception) {
            LocalDateTime.now(zoneId())
        }
    }
}

