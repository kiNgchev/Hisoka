package net.kingchev.event

import dev.kord.common.entity.optional.orEmpty
import dev.kord.core.Kord
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import net.kingchev.command.model.AbstractSubCommand
import net.kingchev.command.model.ICommand
import net.kingchev.service.BotService

public class CommandHandler(kord: Kord) : IListener {
    private val bot = BotService
    init {
        kord.on<GuildChatInputCommandInteractionCreateEvent> {
            val subCommand = interaction.command.data.options.orEmpty().first().name
            val invokedCommandName = interaction.invokedCommandName

            val command = bot.commands[invokedCommandName]
                ?: bot.commands[subCommand]
                ?: bot.groups[invokedCommandName]?.commands?.get(subCommand)
                ?: return@on

            command.execute(this)
        }
    }
}