package net.kingchev.dsl.selectmenu

import dev.kord.core.event.interaction.GuildSelectMenuInteractionCreateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public interface ISelectMenu {
    public suspend fun execute(event: GuildSelectMenuInteractionCreateEvent)

    public companion object {
        public val logger: Logger = LoggerFactory.getLogger(ISelectMenu::class.java)
    }
}