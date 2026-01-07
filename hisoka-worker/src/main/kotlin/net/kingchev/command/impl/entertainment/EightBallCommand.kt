package net.kingchev.command.impl.entertainment

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.EmbedBuilder
import net.kingchev.database.service.getLocale
import net.kingchev.dsl.command.AbstractCommand
import net.kingchev.localization.createDiscordMessage
import net.kingchev.localization.getMessage
import net.kingchev.model.Colors

public class EightBallCommand(private val kord: Kord) : AbstractCommand({
    key("8ball"); description("command.eightball.metadata.description")
}) {
    override fun build(): GlobalChatInputCreateBuilder.() -> Unit = {
        this.apply(super.build())
        string(
            getMessage("command.eightball.metadata.params.ask.name"),
            getMessage("command.eightball.metadata.params.ask.description"),
        ) {
            required = true
            nameLocalizations = createDiscordMessage("command.eightball.metadata.params.ask.name")
            descriptionLocalizations = createDiscordMessage("command.eightball.metadata.params.ask.description")
        }
    }

    override suspend fun validate(event: GuildChatInputCommandInteractionCreateEvent): Boolean = true

    override suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent) {
        val interaction = event.interaction.deferEphemeralResponse()
        val locale = getLocale(event.interaction)

        val choices = arrayOf(
            getMessage("command.eightball.choices.yes", locale),
            getMessage("command.eightball.choices.no", locale),
            getMessage("command.eightball.choices.mostlikely", locale),
            getMessage("command.eightball.choices.maybe", locale),
        )
        val answer = choices.random()

        val embed = EmbedBuilder()
        embed.description = getMessage("command.eightball.description", locale, answer)
        embed.color = Colors.Red

        interaction.respond {
            embeds = mutableListOf(embed)
        }
    }

}