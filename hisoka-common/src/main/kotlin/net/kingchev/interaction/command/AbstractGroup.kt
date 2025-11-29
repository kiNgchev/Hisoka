package net.kingchev.interaction.command

import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import net.kingchev.interaction.command.builder.GroupBuilder
import net.kingchev.interaction.command.data.GroupData
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