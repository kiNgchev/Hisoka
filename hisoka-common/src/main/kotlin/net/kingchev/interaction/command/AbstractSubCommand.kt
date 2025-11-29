package net.kingchev.interaction.command

import dev.kord.rest.builder.interaction.SubCommandBuilder
import net.kingchev.interaction.command.builder.CommandBuilder
import net.kingchev.interaction.command.data.CommandData
import net.kingchev.localization.model.Language
import net.kingchev.localization.service.LocaleService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public abstract class AbstractSubCommand(builder: CommandBuilder.() -> Unit) : ICommand{
    public val data: CommandData = CommandBuilder().apply(builder).build()

    public open fun build(): SubCommandBuilder.() -> Unit = {
        description = LocaleService.getMessage(data.description, Language.EN_US.language)
        descriptionLocalizations = LocaleService.createDiscordMessage(data.description)
    }

    protected companion object {
        public val logger: Logger = LoggerFactory.getLogger(AbstractSubCommand::class.java)
    }
}