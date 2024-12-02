package net.kingchev.command.model.builder

import net.kingchev.command.model.data.CommandData

public class CommandBuilder {
    private var key: String = ""
    private var aliases: Array<String> = arrayOf()
    private var description: String = ""
    private var developerOnly: Boolean = false
    private var guildOnly: Boolean = true
    private var dmOnly: Boolean = false


    public fun key(value: String) {
        key = value
    }

    public fun aliases(value: Array<String>) {
        aliases = value
    }

    public fun alias(value: String) {
        aliases += value
    }

    public fun description(value: String) {
        description = value
    }

    public fun developerOnly(value: Boolean) {
        developerOnly = value
    }

    public fun guildOnly(value: Boolean) {
        guildOnly = value
    }

    public fun dmOnly(value: Boolean) {
        dmOnly = value
    }

    public fun build(): CommandData {
        return CommandData(key, aliases, description, developerOnly, guildOnly, dmOnly)
    }
}