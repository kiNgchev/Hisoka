package net.kingchev.command

import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import org.slf4j.LoggerFactory


public sealed interface ICommand {
    public fun getData(): CommandData {
        return try {
            javaClass.getAnnotation(CommandData::class.java)
        } catch (_: NullPointerException) {
            logger.error("Data annotation in command ${javaClass.name} is not defined")
            CommandData(key = "none", description = "none", group = "none")
        }
    }

    public fun build(): GlobalChatInputCreateBuilder.() -> Unit

    public suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent)

    public companion object {
        private val logger = LoggerFactory.getLogger(ICommand::class.java)
    }
}