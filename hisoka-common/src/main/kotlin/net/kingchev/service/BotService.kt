package net.kingchev.service

import dev.kord.core.Kord
import net.kingchev.command.model.AbstractCommand
import net.kingchev.command.model.AbstractGroup
import net.kingchev.command.model.ICommand
import net.kingchev.event.IListener
import net.kingchev.utils.ReflectionUtils.getSubclasses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.primaryConstructor

public object BotService {
    public val logger: Logger = LoggerFactory.getLogger(BotService::class.java)
    public var commands: MutableMap<String, ICommand> = mutableMapOf()
    public var groups: MutableMap<String, AbstractGroup> = mutableMapOf()

    public fun registerEvents(kord: Kord) {
        getSubclasses<IListener>("net.kingchev.event").forEach {
            try {
                it.primaryConstructor?.call(kord)
                logger.info("Listener [${it.simpleName}] has been registered")
            } catch (_: IllegalArgumentException) {
                logger.error("Error occurred while listener [${it.simpleName}] be initialized")
            }
        }
    }

    public suspend fun registerCommands(kord: Kord) {
        getSubclasses<AbstractCommand>("net.kingchev.command.impl").forEach {
            try {
                val instance = it.primaryConstructor?.call(kord) ?: return@forEach
                commands[instance.getData().key] = instance
                logger.info("Command [${it.simpleName}] has been registered")
            } catch (_: IllegalArgumentException) {
                logger.error("Error occurred while command [${it.simpleName}] be registered")
            }
        }

        commands.forEach { (key, command) ->
            kord.createGlobalChatInputCommand(key, command.getData().description, (command as AbstractCommand).build())
            logger.info("Command [$key] has been registered in discord app")
        }

        kord.getGlobalApplicationCommands().collect {
            if (!commands.containsKey(it.name)) it.delete()
        }
    }

    public suspend fun registerGroupCommands(kord: Kord) {
        getSubclasses<AbstractGroup>("net.kingchev.command.impl").forEach {
            try {
                val instance = it.primaryConstructor?.call(kord) ?: return@forEach
                groups[instance.getData().name] = instance
                logger.info("Command group [${it.simpleName}] has been registered")
            } catch (_: IllegalArgumentException) {
                logger.error("Error occurred while command group [${it.simpleName}] be registered")
            }
        }

        groups.forEach { (key, group) ->
            kord.createGlobalChatInputCommand(key, group.getData().description, group.build())
        }

        kord.getGlobalApplicationCommands().collect {
            if (!groups.containsKey(it.name)) it.delete()
        }
    }
}