package net.kingchev.event.handler

import dev.kord.common.entity.optional.orEmpty
import dev.kord.core.Kord
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import net.kingchev.event.IListener
import net.kingchev.service.BotService

public class CommandHandler(kord: Kord) : IListener {
    private val bot = BotService
    init {
        kord.on<GuildChatInputCommandInteractionCreateEvent> {
            val invokedCommandName = interaction.invokedCommandName
            val subCommand = interaction.command.data.options.orEmpty().firstOrNull()?.name

            val command = bot.commands[invokedCommandName]
                ?: bot.commands[subCommand]
                ?: bot.groups[invokedCommandName]?.commands?.get(subCommand)
                ?: return@on

            if (command.validate(this))
                command.execute(this)
        }
    }
}