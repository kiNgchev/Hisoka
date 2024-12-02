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

    @Suppress("NAME_SHADOWING")
    public suspend fun registerCommands(kord: Kord) {
        getSubclasses<AbstractCommand>("net.kingchev.command.impl").forEach {
            try {
                val instance = it.primaryConstructor?.call(kord) ?: return@forEach
                commands[instance.data.key] = instance
                logger.info("Command [${it.simpleName}] has been registered")
            } catch (_: IllegalArgumentException) {
                logger.error("Error occurred while command [${it.simpleName}] be registered")
            }
        }

        commands.forEach { (key, command) ->
            val command = command as AbstractCommand
            kord.createGlobalChatInputCommand(key, command.data.description, command.build())
            logger.info("Command [$key] has been registered in discord app")
        }

        clearCommands(kord)
    }

    public suspend fun registerGroupCommands(kord: Kord) {
        getSubclasses<AbstractGroup>("net.kingchev.command.impl").forEach {
            try {
                val instance = it.primaryConstructor?.call(kord) ?: return@forEach
                groups[instance.data.name] = instance
                logger.info("Command group [${it.simpleName}] has been registered")
            } catch (_: IllegalArgumentException) {
                logger.error("Error occurred while command group [${it.simpleName}] be registered")
            }
        }

        groups.forEach { (key, group) ->
            kord.createGlobalChatInputCommand(key, group.data.description, group.build())
        }

        clearCommands(kord)
    }

    private suspend fun clearCommands(kord: Kord) {
        kord.getGlobalApplicationCommands().collect {
            if (!groups.containsKey(it.name) and !commands.containsKey(it.name)) {
                it.delete()
                logger.info("Command or commands group [${it.name}] has been deleted")
            }
        }
    }
}