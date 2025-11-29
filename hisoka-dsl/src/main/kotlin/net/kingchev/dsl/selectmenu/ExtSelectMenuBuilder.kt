@file:OptIn(ExperimentalContracts::class)

package net.kingchev.dsl.selectmenu

import dev.kord.rest.builder.component.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public class ExtSelectMenuBuilder {
    public val components: MutableList<SelectMenuBuilder> = mutableListOf()

    public inline fun stringSelect(customId: String, builder: StringSelectBuilder.() -> Unit) {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }

        components.add(StringSelectBuilder(customId).apply(builder))
    }

    public inline fun userSelect(customId: String, builder: UserSelectBuilder.() -> Unit = {}) {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }

        components.add(UserSelectBuilder(customId).apply(builder))
    }

    public inline fun roleSelect(customId: String, builder: RoleSelectBuilder.() -> Unit = {}) {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }

        components.add(RoleSelectBuilder(customId).apply(builder))
    }

    public inline fun mentionableSelect(customId: String, builder: MentionableSelectBuilder.() -> Unit = {}) {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }

        components.add(MentionableSelectBuilder(customId).apply(builder))
    }

    public inline fun channelSelect(customId: String, builder: ChannelSelectBuilder.() -> Unit = {}) {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }

        components.add(ChannelSelectBuilder(customId).apply(builder))
    }

    public fun build(): SelectMenuData {
        return SelectMenuData(components)
    }
}