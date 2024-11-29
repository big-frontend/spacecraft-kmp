package com.spacecraft.kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform