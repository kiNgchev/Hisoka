package net.kingchev.command.impl

import dev.kord.common.Locale
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import dev.kord.rest.builder.interaction.SubCommandBuilder
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.interaction.subCommand
import dev.kord.rest.builder.message.EmbedBuilder
import net.kingchev.command.annotation.CommandData
import net.kingchev.command.annotation.GroupData
import net.kingchev.command.model.AbstractGroup
import net.kingchev.command.model.AbstractSubCommand
import net.kingchev.database.exception.EntryNotFoundException
import net.kingchev.database.service.UserService
import net.kingchev.extensions.idLong
import net.kingchev.localization.model.Language
import net.kingchev.localization.model.parse
import net.kingchev.localization.service.LocaleService
import net.kingchev.model.Colors
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses

@Suppress("NAME_SHADOWING")
@GroupData(name = "locale", description = "Command group for managing locale")
public class LocaleCommandGroup(private val kord: Kord) : AbstractGroup() {
    override fun build(): GlobalChatInputCreateBuilder.() -> Unit = {
        val subcommands = this@LocaleCommandGroup::class.nestedClasses.filter { it.superclasses.contains(AbstractSubCommand::class) }
        for (subcommand in subcommands) {
            val subcommand = subcommand.primaryConstructor?.call(kord) as AbstractSubCommand
            val data = subcommand.getData()
            subCommand(data.key, data.description, subcommand.build())
            commands[data.key] = subcommand
        }
    }

    @CommandData(
        key = "set",
        description = "command.locale.set.metadata.description",
        group = "locale"
    )
    public class SetLocaleCommand(private val kord: Kord) : AbstractSubCommand() {

        override fun build(): SubCommandBuilder.() -> Unit = {
            description = LocaleService.getMessage(getData().description, Language.EN_US.language)
            descriptionLocalizations = mutableMapOf(
                Locale.GERMAN to LocaleService.getMessage(getData().description, Language.DE_DE.language),
                Locale.RUSSIAN to LocaleService.getMessage(getData().description, Language.RU_RU.language)
            )

            string(
                LocaleService.getMessage("command.locale.set.metadata.params.language.name", Language.EN_US.language),
                LocaleService.getMessage("command.locale.set.metadata.params.language.description", Language.EN_US.language)
            ) {
                required = true

                descriptionLocalizations = mutableMapOf(
                    Locale.GERMAN to LocaleService.getMessage("command.locale.set.metadata.params.language.description", Language.DE_DE.language),
                    Locale.RUSSIAN to LocaleService.getMessage("command.locale.set.metadata.params.language.description", Language.RU_RU.language)
                )
                for (lang in Language.entries) {
                    choice(name = "${lang.nativeName} (${lang.englishName})", value = lang.code.lowercase())
                }
            }
        }

        override suspend fun isValid(event: GuildChatInputCommandInteractionCreateEvent): Boolean = true

        override suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent) {
            val interaction = event.interaction.deferPublicResponse()
            val language = event.interaction.command.options[LocaleService.getMessage("command.locale.set.metadata.params.language.name", Language.EN_US.language)]?.value as String

            UserService.setLocale(event.interaction.user.idLong, language)

            val locale = try {
                UserService.getLocale(event.interaction.user.idLong)
            } catch (_: EntryNotFoundException) {
                val user = event.interaction.user
                val entry = UserService.createUser(user.idLong, user.username)
                entry.locale
            }

            val embed = EmbedBuilder()
            embed.description = LocaleService.getMessage("command.locale.set.description", locale, parse(language).nativeName)
            embed.color = Colors.Red

            interaction.respond {
                embeds = mutableListOf(embed)
            }
        }
    }

    @CommandData(
        key = "view",
        description = "command.locale.view.metadata.description",
        group = "locale"
    )
    public class ViewLocaleCommand(private val kord: Kord) : AbstractSubCommand() {
        override fun build(): SubCommandBuilder.() -> Unit = {
            description = LocaleService.getMessage(getData().description, Language.EN_US.language)
            descriptionLocalizations = mutableMapOf(
                Locale.GERMAN to LocaleService.getMessage(getData().description, Language.DE_DE.language),
                Locale.RUSSIAN to LocaleService.getMessage(getData().description, Language.RU_RU.language)
            )
        }

        override suspend fun isValid(event: GuildChatInputCommandInteractionCreateEvent): Boolean = true

        override suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent) {
            val locale = UserService.getLocale(event.interaction.user.idLong)

            val interaction = event.interaction.deferPublicResponse()

            val embed = EmbedBuilder()
            embed.description = LocaleService.getMessage("command.locale.view.description", locale, parse(locale).nativeName)
            embed.color = Colors.Red

            interaction.respond {
                embeds = mutableListOf(embed)
            }
        }
    }
}
