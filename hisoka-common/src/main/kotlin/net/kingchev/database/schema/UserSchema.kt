package net.kingchev.database.schema

import net.kingchev.localization.model.Language
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

public object UserSchema : Table("user") {
    public val id: Column<Long> = long(name = "id")
    public val username: Column<String> = text(name = "username").uniqueIndex()
    public val isPremium: Column<Boolean> = bool(name = "isPremium")
    public val locale: Column<String> = varchar(name = "locale", length = 6).default(Language.EN_US.language)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}