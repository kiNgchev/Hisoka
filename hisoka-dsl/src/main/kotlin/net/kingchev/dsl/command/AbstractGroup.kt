package net.kingchev.dsl.command

import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import org.slf4j.LoggerFactory

public abstract class AbstractGroup(builder: GroupBuilder.() -> Unit) {
    public val data: GroupData = GroupBuilder().apply(builder).build()
    public val commands: HashMap<String, ICommand> = hashMapOf()

    public open fun build(): GlobalChatInputCreateBuilder.() -> Unit = {

    }

    public companion object {
        private val logger = LoggerFactory.getLogger(AbstractGroup::class.java)
    }
}