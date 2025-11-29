package net.kingchev.dsl.button

import dev.kord.core.event.interaction.GuildButtonInteractionCreateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public interface IButton {
    public suspend fun execute(event: GuildButtonInteractionCreateEvent)

    public companion object {
        public val logger: Logger = LoggerFactory.getLogger(IButton::class.java)
    }
}