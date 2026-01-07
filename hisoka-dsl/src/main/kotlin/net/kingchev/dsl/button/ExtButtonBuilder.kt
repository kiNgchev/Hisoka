package net.kingchev.dsl.button

import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.DiscordPartialEmoji

public class ExtButtonBuilder {
    private var id: String = ""
    private var label: String = ""
    private var emoji: DiscordPartialEmoji? = null
    private var style: ButtonStyle = ButtonStyle.Primary
    private var url: String? = null
    private var disabled: Boolean = false

    public fun id(value: String) {
        id = value
    }
    public fun label(value: String) {
        label = value
    }
    public fun emoji(value: DiscordPartialEmoji) {
        emoji = value
    }
    public fun style(value: ButtonStyle) {
        style = value
    }
    public fun url(value: String) {
        url = value
    }
    public fun disabled(value: Boolean) {
        disabled = value
    }

    public fun build(): ButtonData {
        return ButtonData(id, label, emoji, style, url, disabled)
    }
}