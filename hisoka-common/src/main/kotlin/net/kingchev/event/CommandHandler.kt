package net.kingchev.event

import dev.kord.core.Kord
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import net.kingchev.service.BotService

public class CommandHandler(kord: Kord) : IListener {
    private val bot = BotService
    init {
        kord.on<GuildChatInputCommandInteractionCreateEvent> {
            val command = bot.commands[interaction.invokedCommandName] ?: return@on


            command.execute(this)
        }
    }
}