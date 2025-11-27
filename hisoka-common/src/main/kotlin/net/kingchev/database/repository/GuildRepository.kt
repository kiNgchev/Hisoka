package net.kingchev.database.repository

import net.kingchev.database.model.GuildModel
import net.kingchev.database.schema.GuildSchema
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upsert

public object GuildRepository : Repository<GuildSchema, GuildModel>(GuildSchema) {
    override suspend fun upsert(model: GuildModel) {
        schema.upsert(schema.id) {
            it[schema.id] = model.id
            it[schema.name] = model.name
            it[schema.description] = model.description
            it[schema.memberCount] = model.memberCount
            it[schema.roles] = model.roles
            it[schema.isPremium] = model.isPremium
        }
    }

    override suspend fun read(id: Long): GuildModel? {
        return schema.selectAll()
            .where { GuildSchema.id eq id }
            .map { GuildModel(
                it[schema.id],
                it[schema.name],
                it[schema.description],
                it[schema.memberCount],
                it[schema.roles],
                it[schema.isPremium]
            ) }
            .singleOrNull()
    }

    override suspend fun delete(id: Long) {
        schema.deleteWhere { schema.id eq id}
    }
}