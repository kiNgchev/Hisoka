package net.kingchev.database.schema

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

public object UserSchema : Table() {
    public val id: Column<Long> = long(name = "id")
    public val username: Column<String> = varchar(name = "username", length = 50)
    public val balance: Column<Long> = long(name = "balance").default(0)
    public val wins: Column<Long> = long(name = "wins").default(0)
    public val losses: Column<Long> = long(name = "losses").default(0)

    override val tableName: String = "user"
    override val primaryKey: PrimaryKey = PrimaryKey(id, username)
}