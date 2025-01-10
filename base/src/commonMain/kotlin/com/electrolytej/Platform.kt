package com.electrolytej

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform