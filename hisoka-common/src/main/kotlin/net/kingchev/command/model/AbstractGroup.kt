package net.kingchev.command.model

import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import net.kingchev.command.annotation.GroupData
import org.slf4j.LoggerFactory

public abstract class AbstractGroup {
    public val commands: HashMap<String, ICommand> = hashMapOf()

    public fun getData(): GroupData {
        return try {
            javaClass.getAnnotation(GroupData::class.java)
        } catch (_: NullPointerException) {
            logger.error("Data annotation in command ${javaClass.name} is not defined")
            GroupData(name = "none", description = "none")
        }
    }

    public abstract fun build(): GlobalChatInputCreateBuilder.() -> Unit

    public companion object {
        private val logger = LoggerFactory.getLogger(AbstractGroup::class.java)
    }
}