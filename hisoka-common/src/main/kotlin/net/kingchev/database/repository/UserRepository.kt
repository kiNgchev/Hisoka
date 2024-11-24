@file:Suppress("OPT_IN_USAGE")

package net.kingchev.database.repository

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.kingchev.database.model.UserModel
import net.kingchev.database.query
import net.kingchev.database.schema.UserSchema
import net.kingchev.model.Constants.ID_LONG
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

public object UserRepository : Repository<UserSchema, UserModel>(UserSchema) {
    private val default = UserModel(ID_LONG, "Hisoka Morow#6440", 0, 0, 0)

    init {
        GlobalScope.launch {
            create(default)
        }
    }

    public override suspend fun create(model: UserModel): Unit = query {
        schema.upsert(schema.id, onUpdateExclude = listOf(schema.balance)) {
            it[schema.id] = model.id
            it[schema.username] = model.username
            it[schema.balance] = model.balance
            it[schema.wins] = model.wins
            it[schema.losses] = model.losses
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
                it[schema.losses]
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
                it[schema.losses]
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
        }
    }

    public override suspend fun delete(id: Long): Unit = query {
        schema.deleteWhere { schema.id eq id }
    }
}