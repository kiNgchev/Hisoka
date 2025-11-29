package net.kingchev.dsl.modal

import dev.kord.core.event.interaction.GuildModalSubmitInteractionCreateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public interface IModal {
    public suspend fun execute(event: GuildModalSubmitInteractionCreateEvent)

    public companion object {
        public val logger: Logger = LoggerFactory.getLogger(IModal::class.java)
    }
}