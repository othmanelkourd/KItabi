package com.smile.kitabi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform