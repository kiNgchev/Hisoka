package net.kingchev.extensions

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.ModalParentInteractionBehavior
import dev.kord.core.behavior.interaction.modal
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.Channel
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.component.ButtonBuilder
import dev.kord.rest.builder.interaction.RootInputChatBuilder
import dev.kord.rest.builder.interaction.subCommand
import net.kingchev.dsl.button.AbstractButton
import net.kingchev.dsl.command.AbstractGroup
import net.kingchev.dsl.command.AbstractSubCommand
import net.kingchev.dsl.modal.AbstractModal
import net.kingchev.dsl.selectmenu.AbstractSelectMenu
import kotlin.collections.set
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses

public val User.idLong: Long get() = Convertor.toLong(this.id)

public val Member.idLong: Long get() = Convertor.toLong(this.id)

public val Channel.idLong: Long get() = Convertor.toLong(this.id)

public val Guild.idLong: Long get() = Convertor.toLong(this.id)

private object Convertor {
    fun toLong(id: Snowflake): Long = id.value.toLong()
}

public fun ActionRowBuilder.interactionButton(button: AbstractButton) {
    components += ButtonBuilder.InteractionButtonBuilder(button.data.style, button.data.id)
        .apply {
            label = button.data.label
            emoji = button.data.emoji
            disabled = button.data.disabled
        }
}

public suspend fun ModalParentInteractionBehavior.modal(modal: AbstractModal) {
    this.modal(modal.data.title, modal.data.id) {
        actionRow { components += modal.data.fields }
    }
}

public fun ActionRowBuilder.select(select: AbstractSelectMenu) {
    components += select.data.components
}

public fun RootInputChatBuilder.subCommands(kord: Kord, group: AbstractGroup) {
    val subcommands = group::class.nestedClasses.filter { it.superclasses.contains(AbstractSubCommand::class) }
    for (subcommand in subcommands) {
        val subcommand = subcommand.primaryConstructor?.call(kord) as AbstractSubCommand
        val data = subcommand.data
        subCommand(data.key, data.description, subcommand.build())
        group.commands[data.key] = subcommand
    }
}