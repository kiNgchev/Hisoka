package net.kingchev.interaction.modal.builder

import dev.kord.common.entity.TextInputStyle
import dev.kord.rest.builder.component.ActionRowComponentBuilder
import dev.kord.rest.builder.component.TextInputBuilder
import net.kingchev.interaction.modal.data.ModalData
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public class ExtModalBuilder {
    private var id: String = ""
    private var title: String = ""
    private var fields: MutableList<ActionRowComponentBuilder> = mutableListOf()

    public fun id(value: String) {
        id = value
    }

    public fun title(value: String) {
        title = value
    }

    @OptIn(ExperimentalContracts::class)
    public fun textInput(style: TextInputStyle, customId: String, label: String, builder: TextInputBuilder.() -> Unit = {}) {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }

        fields.add(TextInputBuilder(style, customId, label).apply(builder))
    }

    public fun build(): ModalData {
        return ModalData(id, title, fields)
    }
}