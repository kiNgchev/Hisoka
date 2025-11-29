package net.kingchev.extensions

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.interaction.ModalParentInteractionBehavior
import dev.kord.core.behavior.interaction.modal
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.Channel
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.component.ButtonBuilder
import net.kingchev.dsl.button.AbstractButton
import net.kingchev.dsl.modal.AbstractModal
import net.kingchev.dsl.selectmenu.AbstractSelectMenu

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