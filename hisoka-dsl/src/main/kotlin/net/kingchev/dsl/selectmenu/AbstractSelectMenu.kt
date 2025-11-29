package net.kingchev.dsl.selectmenu

import org.slf4j.Logger
import org.slf4j.LoggerFactory

public abstract class AbstractSelectMenu(builder: ExtSelectMenuBuilder.() -> Unit) : ISelectMenu {
    public val data: SelectMenuData = ExtSelectMenuBuilder().apply(builder).build()

    protected companion object {
        public val logger: Logger = LoggerFactory.getLogger(AbstractSelectMenu::class.java)
    }
}