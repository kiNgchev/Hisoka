package net.kingchev.context

import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import dev.kord.core.entity.User
import dev.kord.core.entity.interaction.Interaction
import net.kingchev.localization.Language

public data class InteractionContext(
    val id: String,
    val kord: Kord,
    val interaction: Interaction? = null,
    val guild: Guild? = null,
    val author: User? = null,
    val userLocale: Language,
    val objects: List<Any?> = listOf()
)