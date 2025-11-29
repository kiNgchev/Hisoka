package net.kingchev.interaction.button

import net.kingchev.interaction.button.builder.ExtButtonBuilder
import net.kingchev.interaction.button.data.ButtonData
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public abstract class AbstractButton(builder: ExtButtonBuilder.() -> Unit) : IButton {
    public val data: ButtonData = ExtButtonBuilder().apply(builder).build()

    protected companion object {
        public val logger: Logger = LoggerFactory.getLogger(AbstractButton::class.java)
    }
}