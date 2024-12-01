package net.kingchev.command.model

import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import net.kingchev.command.annotation.CommandData
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public interface ICommand {
    public fun getData(): CommandData {
        return try {
            javaClass.getAnnotation(CommandData::class.java)
        } catch (_: NullPointerException) {
            logger.error("Data annotation in command ${javaClass.name} is not defined")
            CommandData(key = "none", description = "none", group = "none")
        }
    }

    public suspend fun isValid(event: GuildChatInputCommandInteractionCreateEvent): Boolean

    public suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent)

    public companion object {
        public val logger: Logger = LoggerFactory.getLogger(ICommand::class.java)
    }
}