@file:OptIn(ExperimentalDatabaseMigrationApi::class)
package net.kingchev.database.migrations

import MigrationUtils
import net.kingchev.database.schema.UserSchema
import org.jetbrains.exposed.sql.ExperimentalDatabaseMigrationApi

public fun generateUserV2MigrationScript() {
    MigrationUtils.generateMigrationScript(
        UserSchema,
        scriptDirectory = MIGRATIONS_DIRECTORY,
        scriptName = "V2__add__is_premium__column"
    )
}