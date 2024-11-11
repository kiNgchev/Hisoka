package net.kingchev.service

import dev.kord.core.Kord
import net.kingchev.command.ICommand
import net.kingchev.event.IListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.primaryConstructor

public object BotService {
    public val logger: Logger = LoggerFactory.getLogger(BotService::class.java)
    public var commands: MutableMap<String, ICommand> = mutableMapOf()

    public fun registerEvents(kord: Kord) {
        IListener::class.sealedSubclasses.forEach {
            try {
                it.primaryConstructor?.call(kord)
                logger.info("Listener ${it.simpleName} was registered")
            } catch (error: IllegalArgumentException) {
                logger.error(it.simpleName + ": " + error.message)
            }
        }
    }

    public suspend fun registerCommands(kord: Kord) {
        ICommand::class.sealedSubclasses.forEach {
            try {
                val cmd = it.primaryConstructor?.call(kord) ?: return@forEach
                commands[cmd.getData().key] = cmd
                logger.info("Command ${it.simpleName} was added")
            } catch (error: IllegalArgumentException) {
                logger.error(it.simpleName + ": " + error.message)
            }
        }
        commands.forEach { (key, command) ->
            kord.createGlobalChatInputCommand(key, command.getData().description, command.build())
            logger.info("Command $key was registered")
        }
    }

}