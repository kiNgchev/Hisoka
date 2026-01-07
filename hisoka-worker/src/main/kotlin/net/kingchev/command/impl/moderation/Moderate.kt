package net.kingchev.command.impl.moderation

import dev.kord.common.entity.*
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
import net.kingchev.context.getContext
import net.kingchev.database.service.getLocale
import net.kingchev.dsl.button.AbstractButton
import net.kingchev.dsl.command.AbstractCommand
import net.kingchev.dsl.modal.AbstractModal
import net.kingchev.extensions.*
import net.kingchev.localization.createDiscordMessage
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
            getMessage("command.moderate.metadata.params.target.name"),
            getMessage("command.moderate.metadata.params.target.description"),
        ) {
            required = true
            nameLocalizations = createDiscordMessage("command.moderate.metadata.params.target.name")
            descriptionLocalizations = createDiscordMessage("command.moderate.metadata.params.target.description")
        }
        string(
            getMessage("command.moderate.metadata.params.reason.name"),
            getMessage("command.moderate.metadata.params.reason.description"),
        ) {
            nameLocalizations = createDiscordMessage("command.moderate.metadata.params.reason.name")
            descriptionLocalizations = createDiscordMessage("command.moderate.metadata.params.reason.description")
        }
    }

    override suspend fun validate(event: GuildChatInputCommandInteractionCreateEvent): Boolean = true

    override suspend fun execute(event: GuildChatInputCommandInteractionCreateEvent) {
        val interaction = event.interaction.deferPublicResponse()
        val locale = getLocale(event.interaction)

        val targetId: Snowflake = event.getOption(getMessage("command.moderate.metadata.params.target.name"))
            ?: return
        val reason: String? = event.getOption(getMessage("command.moderate.metadata.params.reason.name"))

        val target = event.interaction.getGuild().getMember(targetId)
        val moderator = event.interaction.user

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
        val context: InteractionContext =
            getContext("moderate_${event.interaction.getGuild().idLong}_${event.interaction.user.idLong}")
                ?: return

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

        val context: InteractionContext =
            getContext("moderate_${event.interaction.getGuild().idLong}_${event.interaction.user.idLong}")
                ?: return

        val target = context.objects[0] as Member
        val reason = if (context.objects.size > 1) {
            context.objects[1] as? String
        } else null

        val time: String = event.getInput("time") ?: return
        val parsedTime = try {
            parseTime(time)
        } catch (_: IllegalArgumentException) {
            interaction.respond {
                content = getMessage("command.moderate.moderator.invalid.time", context.userLocale)
            }
            return
        }

        target.edit {
            communicationDisabledUntil = Clock.System.now() + parsedTime
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
        val context: InteractionContext =
            getContext("moderate_${event.interaction.getGuild().idLong}_${event.interaction.user.idLong}")
                ?: return

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
        val context: InteractionContext =
            getContext("moderate_${event.interaction.getGuild().idLong}_${event.interaction.user.idLong}") ?: return

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