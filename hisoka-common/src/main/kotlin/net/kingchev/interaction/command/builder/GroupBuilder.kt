package net.kingchev.interaction.command.builder

import net.kingchev.interaction.command.data.GroupData

public class GroupBuilder {
    private var name: String = ""
    private var description: String = ""

    public fun name(value: String) {
        name = value
    }

    public fun description(value: String) {
        description = value
    }

    public fun build(): GroupData {
        return GroupData(name, description)
    }
}