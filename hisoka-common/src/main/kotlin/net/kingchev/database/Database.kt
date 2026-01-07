package net.kingchev.database

import MigrationUtils
import kotlinx.coroutines.Dispatchers
import net.kingchev.structure.Initializable
import net.kingchev.structure.delegation.property
import net.kingchev.utils.ReflectionUtils
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.jetbrains.exposed.sql.Database as Exposed

public object DatabaseInitializer : Initializable {
    private val url: String by property("database.connection.url")
    private val driver = org.postgresql.Driver::class.qualifiedName!!
    private val username: String by property("database.connection.username")
    private val password: String by property("database.connection.password")

    public val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun initialize() {
        val schemas = ReflectionUtils.getSubclasses<Table>("net.kingchev.database.schema")
        val connection = Exposed.connect(
            url,
            driver,
            user = username,
            password = password,
        )

        transaction(connection) {
            SchemaUtils.setSchema(Schema("public"))
            for (schema in schemas) {
                MigrationUtils.statementsRequiredForDatabaseMigration(schema.objectInstance ?: throw IllegalStateException("Schema ${schema.qualifiedName} is not object"))
            }
        }
        logger.info("Database has been initialized")
    }
}

public suspend fun <T> query (block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }