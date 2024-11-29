package com.spacecraft.a_module

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform