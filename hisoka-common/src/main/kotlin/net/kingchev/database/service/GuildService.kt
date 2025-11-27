package net.kingchev.database.service

import net.kingchev.database.exception.EntryNotFoundException
import net.kingchev.database.model.GuildModel
import net.kingchev.database.repository.GuildRepository

public object GuildService {
    private val repository = GuildRepository

    public suspend fun changeName(id: Long, name: String) {
        val result = repository.read(id)
        val model: GuildModel = result?.copy(name = name) ?: createGuild(id, name)

        repository.upsert(model)
    }

    public suspend fun createGuild(id: Long, name: String, description: String? = null, memberCount: Int = 0, roles: List<Long> = emptyList<Long>(), isPremium: Boolean = false): GuildModel {
        val model = GuildModel(id, name, description, memberCount, roles, isPremium)
        repository.upsert(model)
        return model
    }

    @Throws(EntryNotFoundException::class)
    private suspend fun getGuild(id: Long): GuildModel {
        return repository.read(id) ?: throw EntryNotFoundException("The user with id `$id` not found")
    }

    private suspend fun getGuildWithCreate(id: Long, name: String): GuildModel {
        return repository.read(id) ?: createGuild(id, name)
    }
}