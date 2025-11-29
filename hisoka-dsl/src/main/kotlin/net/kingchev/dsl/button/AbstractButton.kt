package net.kingchev.dsl.button

import net.kingchev.dsl.button.ExtButtonBuilder
import net.kingchev.dsl.button.ButtonData
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public abstract class AbstractButton(builder: ExtButtonBuilder.() -> Unit) : IButton {
    public val data: ButtonData = ExtButtonBuilder().apply(builder).build()

    protected companion object {
        public val logger: Logger = LoggerFactory.getLogger(AbstractButton::class.java)
    }
}