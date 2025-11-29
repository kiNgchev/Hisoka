package net.kingchev.event.handler

import dev.kord.core.Kord
import dev.kord.core.event.interaction.GuildButtonInteractionCreateEvent
import dev.kord.core.event.interaction.GuildModalSubmitInteractionCreateEvent
import dev.kord.core.event.interaction.GuildSelectMenuInteractionCreateEvent
import dev.kord.core.on
import net.kingchev.event.IListener
import net.kingchev.service.BotService

public class ButtonHandler(kord: Kord) : IListener {
    private val bot = BotService
    init {
        kord.on<GuildButtonInteractionCreateEvent> {
            val button = bot.components.buttons[interaction.componentId] ?: return@on
            button.execute(this)
        }
    }
}

public class ModalHandler(kord: Kord) : IListener {
    private val bot = BotService
    init {
        kord.on<GuildModalSubmitInteractionCreateEvent> {
            val modal = bot.components.modals[interaction.modalId] ?: return@on
            modal.execute(this)
        }
    }
}

public class SelectMenyHandler(kord: Kord) : IListener {
    private val bot = BotService
    init {
        kord.on<GuildSelectMenuInteractionCreateEvent> {
            val select = bot.components.selects[interaction.componentId] ?: return@on
            select.execute(this)
        }
    }
}