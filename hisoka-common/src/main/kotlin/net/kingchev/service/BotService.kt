package net.kingchev.service

import dev.kord.core.Kord
import net.kingchev.dsl.event.IListener
import net.kingchev.dsl.button.AbstractButton
import net.kingchev.dsl.command.AbstractCommand
import net.kingchev.dsl.command.AbstractGroup
import net.kingchev.dsl.command.ICommand
import net.kingchev.dsl.modal.AbstractModal
import net.kingchev.dsl.selectmenu.AbstractSelectMenu
import net.kingchev.service.BotService.logger
import net.kingchev.utils.ReflectionUtils.getSubclasses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.primaryConstructor

public object BotService {
    public val logger: Logger = LoggerFactory.getLogger(BotService::class.java)
    public var commands: MutableMap<String, ICommand> = mutableMapOf()
        private set
    public var components: ComponentRegistrar = ComponentRegistrar
        private set

    public var groups: MutableMap<String, AbstractGroup> = mutableMapOf()
        private set

    public fun registerEvents(kord: Kord) {
        getSubclasses<IListener>("net.kingchev.event", "net.kingchev.handler", "net.kingchev.command").forEach {
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

public object ComponentRegistrar {
    public var buttons: MutableMap<String, AbstractButton> = mutableMapOf()
        private set
    public var modals: MutableMap<String, AbstractModal> = mutableMapOf()
        private set
    public var selects: MutableMap<String, AbstractSelectMenu> = mutableMapOf()
        private set

    public fun registerButtons() {
        getSubclasses<AbstractButton>().forEach {
            try {
                val instance = it.primaryConstructor?.call() ?: return@forEach
                buttons[instance.data.id] = instance
                logger.info("Button [${it.simpleName}] has been registered")
            } catch (_: IllegalArgumentException) {
                logger.error("Error occurred while button [${it.simpleName}] be registered")
            }
        }
    }

    public fun registerModals() {
        getSubclasses<AbstractModal>().forEach {
            try {
                val instance = it.primaryConstructor?.call() ?: return@forEach
                modals[instance.data.id] = instance
                logger.info("Modal [${it.simpleName}] has been registered")
            } catch (_: IllegalArgumentException) {
                logger.error("Error occurred while modal [${it.simpleName}] be registered")
            }
        }
    }

    public fun registerSelects() {
        getSubclasses<AbstractSelectMenu>().forEach {
            try {
                val instance = it.primaryConstructor?.call() ?: return@forEach
                instance.data.components.forEach { component ->
                    selects[component.customId] = instance
                }
                logger.info("Select menu [${it.simpleName}] has been registered")
            } catch (_: IllegalArgumentException) {
                logger.error("Error occurred while select menu [${it.simpleName}] be registered")
            }
        }
    }
}