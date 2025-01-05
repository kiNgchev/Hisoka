package net.kingchev.database.schema

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

public object GuildSchema : Table("guild") {
    public val id: Column<Long> = long(name = "id")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}