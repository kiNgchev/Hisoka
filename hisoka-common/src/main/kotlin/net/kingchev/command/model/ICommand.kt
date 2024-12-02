package net.kingchev.command.model

import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public interface ICommand {
    public suspend fun isValid(event: GuildChatInputCommandInteractionCreateEvent): Boolean

    public suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent)

    public companion object {
        public val logger: Logger = LoggerFactory.getLogger(ICommand::class.java)
    }
}