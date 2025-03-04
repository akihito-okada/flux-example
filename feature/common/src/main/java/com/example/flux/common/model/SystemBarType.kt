package com.example.flux.common.model

enum class SystemBarType {
    Primary,
    PrimaryDark,
    PrimaryTransparent,
    PrimaryDarkTransparent,
    NowPlaying,
    Unknown,
    ;

    val isNowPlaying get() = this == NowPlaying
}
