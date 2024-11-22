package net.kingchev.database.repository

import net.kingchev.database.model.UserModel
import org.jetbrains.exposed.sql.Table

public abstract class Repository<T : Table>(protected val schema: T) {
    public abstract suspend fun create(model: UserModel): Unit

    public abstract suspend fun read(id: Long): UserModel?

    public abstract suspend fun update(id: Long, model: UserModel): Unit

    public abstract suspend fun delete(id: Long): Unit
}