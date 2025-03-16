package net.kingchev.database.schema

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.LongColumnType
import org.jetbrains.exposed.sql.Table

public object GuildSchema : Table("guild") {
    public val id: Column<Long> = long(name = "id")

    public val name: Column<String> = varchar(name = "name", length = 100)
    public val description: Column<String?> = text(name = "description").nullable()
    public val memberCount: Column<Int> = integer(name = "member_count")
    public val roles: Column<List<Long>> = array(name = "roles", columnType = LongColumnType())
    public val isPremium: Column<Boolean> = bool(name = "is_premium")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}