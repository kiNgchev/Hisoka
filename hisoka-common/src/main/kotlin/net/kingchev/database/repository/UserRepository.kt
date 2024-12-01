@file:Suppress("OPT_IN_USAGE")

package net.kingchev.database.repository

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.kingchev.database.model.UserModel
import net.kingchev.database.query
import net.kingchev.database.schema.UserSchema
import net.kingchev.localization.model.Language
import net.kingchev.model.BotMetadata.ID_LONG
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.upsert

public object UserRepository : Repository<UserSchema, UserModel>(UserSchema) {
    private val default = UserModel(ID_LONG, "Hisoka Morow#6440", 0, 0, 0, Language.EN_US.language)

    init {
        GlobalScope.launch {
            if (read(default.id) == null) create(default)
        }
    }

    public override suspend fun create(model: UserModel): Unit = query {
        schema.upsert(schema.id) {
            it[schema.id] = model.id
            it[schema.username] = model.username
            it[schema.balance] = model.balance
            it[schema.wins] = model.wins
            it[schema.losses] = model.losses
            it[schema.locale] = model.locale
        }
    }

    public override suspend fun read(id: Long): UserModel? = query {
        schema.selectAll()
            .where { schema.id eq id }
            .map { UserModel(
                it[schema.id],
                it[schema.username],
                it[schema.balance],
                it[schema.wins],
                it[schema.losses],
                it[schema.locale]
            ) }
            .singleOrNull()
    }

    public suspend fun read(username: String): UserModel? = query {
        schema.selectAll()
            .where { schema.username eq username }
            .map { UserModel(
                it[schema.id],
                it[schema.username],
                it[schema.balance],
                it[schema.wins],
                it[schema.losses],
                it[schema.locale]
            ) }
            .singleOrNull()
    }

    public override suspend fun update(id: Long, model: UserModel): Unit = query {
        schema.update({ schema.id eq id }) {
            it[schema.id] = model.id
            it[schema.username] = model.username
            it[schema.balance] = model.balance
            it[schema.wins] = model.wins
            it[schema.losses] = model.losses
            it[schema.locale] = model.locale
        }
    }

    public override suspend fun delete(id: Long): Unit = query {
        schema.deleteWhere { schema.id eq id }
    }
}