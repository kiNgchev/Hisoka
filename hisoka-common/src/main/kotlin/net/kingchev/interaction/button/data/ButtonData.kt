package net.kingchev.interaction.button.data

import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.DiscordPartialEmoji

public data class ButtonData(
    val id: String,
    val label: String,
    val emoji: DiscordPartialEmoji? = null,
    val style: ButtonStyle,
    val url: String? = null,
    val disabled: Boolean = false,
)