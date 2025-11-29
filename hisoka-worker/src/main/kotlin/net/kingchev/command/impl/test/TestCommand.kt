package net.kingchev.command.impl.test

import dev.kord.common.entity.TextInputStyle
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildButtonInteractionCreateEvent
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.GuildModalSubmitInteractionCreateEvent
import dev.kord.core.event.interaction.GuildSelectMenuInteractionCreateEvent
import dev.kord.rest.builder.component.option
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.actionRow
import net.kingchev.extensions.interactionButton
import net.kingchev.extensions.modal
import net.kingchev.extensions.select
import net.kingchev.interaction.button.AbstractButton
import net.kingchev.interaction.command.AbstractCommand
import net.kingchev.interaction.modal.AbstractModal
import net.kingchev.interaction.selectmenu.AbstractSelectMenu
import net.kingchev.model.Colors

public class TestCommand(private val kord: Kord) : AbstractCommand({
    key("test"); description("test")
}) {

    override suspend fun validate(event: GuildChatInputCommandInteractionCreateEvent): Boolean = true

    override suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent) {
        val interaction = event.interaction.deferEphemeralResponse()

        val embed = EmbedBuilder()
        embed.description = "test"
        embed.color = Colors.Red

        interaction.respond {
            embeds = mutableListOf(embed)
            actionRow {
                interactionButton(TestButton())
            }
            actionRow {
                select(TestSelect())
            }
        }
    }
}

public class TestButton : AbstractButton({ id("test_button"); label("test") }) {
    override suspend fun execute(event: GuildButtonInteractionCreateEvent) {
        val interaction = event.interaction
        interaction.modal(TestModal())
    }
}

public class TestModal : AbstractModal({
    id("test_modal")
    title("Test Modal")
    textInput(TextInputStyle.Paragraph, "first", "test")
}) {
    override suspend fun execute(event: GuildModalSubmitInteractionCreateEvent) {
        event.interaction.deferEphemeralResponse().respond {
            content = "test"
        }
    }

}

public class TestSelect : AbstractSelectMenu({
    stringSelect("test") {
        option("test", "test")
    }
}) {
    override suspend fun execute(event: GuildSelectMenuInteractionCreateEvent) {
        event.interaction.respondEphemeral { content = "test" }
    }
}