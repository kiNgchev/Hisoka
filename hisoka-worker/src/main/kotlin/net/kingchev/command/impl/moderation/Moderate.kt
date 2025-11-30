package net.kingchev.command.impl.moderation

import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.TextInputStyle
import dev.kord.core.Kord
import dev.kord.core.behavior.ban
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.Member
import dev.kord.core.event.interaction.GuildButtonInteractionCreateEvent
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.GuildModalSubmitInteractionCreateEvent
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.interaction.user
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.actionRow
import net.kingchev.context.ContextHolder
import net.kingchev.context.InteractionContext
import net.kingchev.database.exception.EntryNotFoundException
import net.kingchev.database.service.UserService
import net.kingchev.dsl.button.AbstractButton
import net.kingchev.dsl.command.AbstractCommand
import net.kingchev.dsl.modal.AbstractModal
import net.kingchev.extensions.button
import net.kingchev.extensions.idLong
import net.kingchev.extensions.modal
import net.kingchev.localization.Language
import net.kingchev.localization.LocaleService
import net.kingchev.localization.getMessage
import net.kingchev.model.Colors
import net.kingchev.utils.parseTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

public class Moderate(private val kord: Kord) : AbstractCommand({
    key("moderate")
    description("command.moderate.metadata.description")
}) {
    private val holder = ContextHolder
    override fun build(): GlobalChatInputCreateBuilder.() -> Unit = {
        this.apply(super.build())
        defaultMemberPermissions = Permissions(Permission.Administrator)
        user(
            getMessage("command.moderate.metadata.params.target.name", Language.EN_US),
            getMessage("command.moderate.metadata.params.target.description", Language.EN_US),
        ) {
            required = true
            nameLocalizations = LocaleService.createDiscordMessage("command.moderate.metadata.params.target.name")
            descriptionLocalizations = LocaleService.createDiscordMessage("command.moderate.metadata.params.target.description")
        }
        string(
            getMessage("command.moderate.metadata.params.reason.name", Language.EN_US),
            getMessage("command.moderate.metadata.params.reason.description", Language.EN_US),
        ) {
            nameLocalizations = LocaleService.createDiscordMessage("command.moderate.metadata.params.reason.name")
            descriptionLocalizations = LocaleService.createDiscordMessage("command.moderate.metadata.params.reason.description")
        }
    }

    override suspend fun validate(event: GuildChatInputCommandInteractionCreateEvent): Boolean = true

    override suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent) {
        val interaction = event.interaction.deferPublicResponse()
        val locale = try {
            UserService.getLocale(event.interaction.user.idLong)
        } catch (_: EntryNotFoundException) {
            val user = event.interaction.user
            val entry = UserService.createUser(user.idLong, user.username)
            entry.locale
        }


        val target = event.interaction.getGuild().getMember(event.interaction.command.options[getMessage("command.moderate.metadata.params.target.name", Language.EN_US)]?.value as Snowflake)
        val moderator = event.interaction.user
        val reason = event.interaction.command.options[getMessage("command.moderate.metadata.params.reason.name", Language.EN_US)]?.value as? String

        val embed = EmbedBuilder()
        embed.field { name = getMessage("command.moderate.moderator", locale); value = moderator.mention}
        embed.field { name = getMessage("command.moderate.target", locale); value = target.mention }
        if (reason != null)
            embed.field { name = getMessage("command.moderate.reason", locale); value = reason }
        embed.color = Colors.Red

        val context = InteractionContext(
            id = "moderate_${event.interaction.getGuild().idLong}_${event.interaction.user.idLong}",
            kord = kord,
            interaction = event.interaction,
            guild = event.interaction.getGuild(),
            author = event.interaction.user,
            userLocale = locale,
            objects = mutableListOf(target, reason),
        )
        holder[context.id] = context

        interaction.respond {
            embeds = mutableListOf(embed)
            actionRow {
                button(MuteButton())
                button(KickButton())
                button(BanButton())
            }
        }
    }
}

public class MuteButton : AbstractButton({
    id("mute_button"); label("Mute")
}) {
    override suspend fun execute(event: GuildButtonInteractionCreateEvent) {
        val context: InteractionContext? = try {
            ContextHolder["moderate_${event.interaction.getGuild().idLong}_${event.interaction.user.idLong}"]
        } catch (_: Exception) {
            null
        }

        if (context == null)
            return

        event.interaction.modal(MuteModal())
    }
}

public class MuteModal : AbstractModal({
    id("mute_modal")
    title("Mute member")
    textInput(TextInputStyle.Short, "time", "Mute time") { placeholder = "e.g. 1h" }
}) {
    @OptIn(ExperimentalTime::class)
    override suspend fun execute(event: GuildModalSubmitInteractionCreateEvent) {
        val interaction = event.interaction.deferEphemeralResponse()

        val context: InteractionContext? = try {
            ContextHolder["moderate_${event.interaction.getGuild().idLong}_${event.interaction.user.idLong}"]
        } catch (_: Exception) {
            null
        }

        if (context == null)
            return

        val target = context.objects[0] as Member
        val reason = if (context.objects.size > 1) {
            context.objects[1] as? String
        } else null
        val time = event.interaction.textInputs["time"]?.value as String

        target.edit {
            communicationDisabledUntil = Clock.System.now() + parseTime(time)
            this.reason = reason
        }
        interaction.respond {
            content = getMessage("command.moderate.mute", context.userLocale, target.mention)
        }
    }
}

public class KickButton : AbstractButton({
    id("kick_button"); label("Kick"); style(ButtonStyle.Success)
}) {
    override suspend fun execute(event: GuildButtonInteractionCreateEvent) {
        val context: InteractionContext? = try {
            ContextHolder["moderate_${event.interaction.getGuild().idLong}_${event.interaction.user.idLong}"]
        } catch (_: Exception) {
            null
        }

        if (context == null)
            return

        val target = context.objects[0] as Member
        val reason = if (context.objects.size > 1) {
            context.objects[1] as? String
        } else null

        context.guild?.kick(target.id, reason = reason)
        getMessage("command.moderate.kick", context.userLocale, target.mention)
    }
}

public class BanButton : AbstractButton({
    id("ban_button"); label("Ban"); style(ButtonStyle.Danger)
}) {
    override suspend fun execute(event: GuildButtonInteractionCreateEvent) {
        val context: InteractionContext? = try {
            ContextHolder["moderate_${event.interaction.getGuild().idLong}_${event.interaction.user.idLong}"]
        } catch (_: Exception) {
            null
        }

        if (context == null)
            return

        val target = context.objects[0] as Member
        val reason = if (context.objects.size > 1) {
            context.objects[1] as? String
        } else null

        context.guild?.ban(target.id) {
            this.reason = reason
        }
        event.interaction.deferPublicResponse().respond {
            content = getMessage("command.moderate.ban", context.userLocale, target.mention)
        }
    }
}