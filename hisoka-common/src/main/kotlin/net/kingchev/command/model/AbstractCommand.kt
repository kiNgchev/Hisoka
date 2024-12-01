package net.kingchev.command.model

import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public abstract class AbstractCommand : ICommand {
    public abstract fun build(): GlobalChatInputCreateBuilder.() -> Unit

    protected companion object {
        public val logger: Logger = LoggerFactory.getLogger(AbstractCommand::class.java)
    }
}