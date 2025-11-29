package net.kingchev.context.model

import dev.kord.core.entity.Guild
import dev.kord.core.entity.User
import dev.kord.core.entity.interaction.Interaction
import net.kingchev.localization.model.Language

public data class InteractionContext(
    val id: String,
    val interaction: Interaction? = null,
    val guild: Guild? = null,
    val author: User? = null,
    val userLocale: Language,
    val objects: List<Any> = listOf()
)