package net.kingchev.command.impl

import dev.kord.common.Locale
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import net.kingchev.command.annotation.CommandData
import net.kingchev.command.model.AbstractCommand
import net.kingchev.database.exception.EntryNotFoundException
import net.kingchev.database.service.UserService
import net.kingchev.extensions.idLong
import net.kingchev.localization.model.Language
import net.kingchev.localization.service.LocaleService
import net.kingchev.model.BotMetadata
import net.kingchev.model.Colors
import java.util.concurrent.atomic.AtomicLong
import kotlin.time.DurationUnit


@CommandData(
    key = "ping",
    description = "command.ping.metadata.description",
    group = ""
)
public class PingCommand(private val kord: Kord) : AbstractCommand() {
    override fun build(): GlobalChatInputCreateBuilder.() -> Unit = {
        description = LocaleService.getMessage(getData().description, Language.EN_US.language)
        descriptionLocalizations = mutableMapOf(
            Locale.GERMAN to LocaleService.getMessage(getData().description, Language.DE_DE.language),
            Locale.RUSSIAN to LocaleService.getMessage(getData().description, Language.RU_RU.language)
        )
    }

    override suspend fun isValid(event: GuildChatInputCommandInteractionCreateEvent): Boolean = true

    override suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent) {
        val restPing = getRestPing(event.kord)
        val interaction = event.interaction.deferPublicResponse()
        val embed = EmbedBuilder()

        embed.field {
            name = "REST"
            value = "```${restPing}ms```"
            inline = true
        }
        embed.field {
            name = "WS"
            value = "```${kord.gateway.averagePing?.toInt(DurationUnit.MILLISECONDS).toString()}ms```"
            inline = true
        }
        embed.color = Colors.Red

        interaction.respond {
            embeds = mutableListOf(embed)
        }
    }

    private suspend fun getRestPing(kord: Kord): Long {
        val time = AtomicLong(System.currentTimeMillis())
        kord.rest.user.getUser(Snowflake(BotMetadata.ID_STRING))
        return System.currentTimeMillis() - time.get()
    }
}