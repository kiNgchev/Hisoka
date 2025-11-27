package net.kingchev.database.repository

import org.jetbrains.exposed.sql.Table

public abstract class Repository<T : Table, M>(protected val schema: T) {
    public abstract suspend fun upsert(model: M): Unit

    public abstract suspend fun read(id: Long): M?

    public abstract suspend fun delete(id: Long): Unit
}