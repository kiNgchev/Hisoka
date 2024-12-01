package net.kingchev.command.model

import dev.kord.rest.builder.interaction.SubCommandBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public abstract class AbstractSubCommand : ICommand{

    public abstract fun build(): SubCommandBuilder.() -> Unit

    protected companion object {
        public val logger: Logger = LoggerFactory.getLogger(AbstractSubCommand::class.java)
    }
}