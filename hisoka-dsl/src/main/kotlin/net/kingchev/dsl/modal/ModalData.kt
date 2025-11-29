package net.kingchev.dsl.modal

import dev.kord.rest.builder.component.ActionRowComponentBuilder

public data class ModalData(
    val id: String,
    val title: String,
    val fields: List<ActionRowComponentBuilder>
)