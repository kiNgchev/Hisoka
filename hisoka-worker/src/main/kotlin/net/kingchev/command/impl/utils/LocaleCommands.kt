package net.kingchev.command.impl.utils

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import dev.kord.rest.builder.interaction.SubCommandBuilder
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.interaction.subCommand
import dev.kord.rest.builder.message.EmbedBuilder
import net.kingchev.command.model.AbstractGroup
import net.kingchev.command.model.AbstractSubCommand
import net.kingchev.database.exception.EntryNotFoundException
import net.kingchev.database.service.UserService
import net.kingchev.extensions.idLong
import net.kingchev.localization.model.Language
import net.kingchev.localization.model.parse
import net.kingchev.localization.service.LocaleService
import net.kingchev.localization.service.getMessage
import net.kingchev.model.Colors
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses

@Suppress("NAME_SHADOWING")
public class LocaleCommandGroup(private val kord: Kord) : AbstractGroup({ name("locale"); description("The group of commands for managing your bot interface locale") }) {
    override fun build(): GlobalChatInputCreateBuilder.() -> Unit = {
        description = getMessage(data.name, Language.EN_US.code)
        val subcommands = this@LocaleCommandGroup::class.nestedClasses.filter { it.superclasses.contains(AbstractSubCommand::class) }
        for (subcommand in subcommands) {
            val subcommand = subcommand.primaryConstructor?.call(kord) as AbstractSubCommand
            val data = subcommand.data
            subCommand(data.key, data.description, subcommand.build())
            commands[data.key] = subcommand
        }
    }

    public class SetLocaleCommand(private val kord: Kord) : AbstractSubCommand({ key("set");description("command.locale.set.metadata.description") }) {

        override fun build(): SubCommandBuilder.() -> Unit = {
            this.apply(super.build())

            string(
                getMessage("command.locale.set.metadata.params.language.name", Language.EN_US),
                getMessage("command.locale.set.metadata.params.language.description", Language.EN_US)
            ) {
                required = true

                descriptionLocalizations = LocaleService.createDiscordMessage("command.locale.set.metadata.params.language.description")
                for (lang in Language.entries) {
                    choice(name = "${lang.nativeName} (${lang.englishName})", value = lang.code.lowercase())
                }
            }
        }

        override suspend fun validate(event: GuildChatInputCommandInteractionCreateEvent): Boolean = true

        override suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent) {
            val interaction = event.interaction.deferPublicResponse()
            val language = event.interaction.command.options[getMessage("command.locale.set.metadata.params.language.name", Language.EN_US)]?.value as String

            UserService.setLocale(event.interaction.user.idLong, language)

            val locale = try {
                UserService.getLocale(event.interaction.user.idLong)
            } catch (_: EntryNotFoundException) {
                val user = event.interaction.user
                val entry = UserService.createUser(user.idLong, user.username)
                entry.locale
            }

            val embed = EmbedBuilder()
            embed.description = getMessage("command.locale.set.description", locale, parse(language).nativeName)
            embed.color = Colors.Red

            interaction.respond {
                embeds = mutableListOf(embed)
            }
        }
    }

    public class ViewLocaleCommand(private val kord: Kord) : AbstractSubCommand({ key("view"); description("command.locale.view.metadata.description") }) {
        override suspend fun validate(event: GuildChatInputCommandInteractionCreateEvent): Boolean = true

        override suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent) {
            val locale = UserService.getLocale(event.interaction.user.idLong)

            val interaction = event.interaction.deferPublicResponse()

            val embed = EmbedBuilder()
            embed.description = getMessage("command.locale.view.description", locale, parse(locale).nativeName)
            embed.color = Colors.Red

            interaction.respond {
                embeds = mutableListOf(embed)
            }
        }
    }
}
