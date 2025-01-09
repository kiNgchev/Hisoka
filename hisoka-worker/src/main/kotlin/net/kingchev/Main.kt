@file:JvmName("Main")

package net.kingchev

import net.kingchev.service.DiscordService
import net.kingchev.structure.Initializer

public suspend fun main() {
    banner()
    Initializer.init()
    DiscordService().login()
}

public fun banner() {
    val banner = Thread
        .currentThread()
        .contextClassLoader
        .getResource("banner.txt")
        ?.readText() ?: return
    println(banner)
}