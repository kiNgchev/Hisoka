@file:OptIn(ExperimentalDatabaseMigrationApi::class)

package net.kingchev.database.migrations

import net.kingchev.database.schema.UserSchema
import org.jetbrains.exposed.sql.ExperimentalDatabaseMigrationApi

public fun generateGuildV3MigrationScript() {
    MigrationUtils.generateMigrationScript(
        UserSchema,
        scriptDirectory = MIGRATIONS_DIRECTORY,
        scriptName = "V3__add__is_premium__column"
    )
}