package net.kingchev.dsl.selectmenu

import dev.kord.rest.builder.component.SelectMenuBuilder

public data class SelectMenuData(
    val components: MutableList<SelectMenuBuilder>
)