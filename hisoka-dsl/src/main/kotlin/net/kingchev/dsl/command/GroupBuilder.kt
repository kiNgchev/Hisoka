package net.kingchev.dsl.command

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