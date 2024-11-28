package net.kingchev.command

import dev.kord.common.Locale
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import dev.kord.rest.builder.interaction.user
import dev.kord.rest.builder.message.EmbedBuilder
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
    description = "",
    group = "utils"
)
public class PingCommand(private val kord: Kord) : ICommand {
    override fun build(): GlobalChatInputCreateBuilder.() -> Unit = {
        description = LocaleService.getMessage("command.ping.metadata.description", Language.EN_US.language)
        descriptionLocalizations = mutableMapOf(
            Locale.GERMAN to LocaleService.getMessage("command.ping.metadata.description", Language.DE_DE.language),
            Locale.RUSSIAN to LocaleService.getMessage("command.ping.metadata.description", Language.RU_RU.language)
        )
    }

    override suspend fun isValid(event: GuildChatInputCommandInteractionCreateEvent): Boolean = true

    override suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent) {
        val locale = try {
            UserService.getLocale(event.interaction.user.idLong)
        } catch (_: EntryNotFoundException) {
            val user = event.interaction.user
            val entry = UserService.createUser(user.idLong, user.username)
            entry.locale
        }
        val restPing = getRestPing(event.kord)
        val interaction = event.interaction.deferPublicResponse()
        val embed = EmbedBuilder()

        embed.title = LocaleService.getMessage("command.ping.title", locale)
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
        embed.footer {
            icon = event.interaction.guild.asGuild().icon?.cdnUrl?.toUrl()
            text = "| " + event.interaction.guild.asGuild().name
        }

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