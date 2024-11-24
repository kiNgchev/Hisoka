package net.kingchev.database.service

import net.kingchev.database.exception.EntryNotFoundException
import net.kingchev.database.model.UserModel
import net.kingchev.database.repository.UserRepository
import kotlin.jvm.Throws

public object UserService {
    private val repository = UserRepository

    @Throws(EntryNotFoundException::class)
    public suspend fun changeUsername(username: String) {
        val result = getUser(username)
        val model = result.copy(username = username)
        repository.update(model.id, model)
    }

    public suspend fun changeUsername(id: Long, username: String) {
        val result = repository.read(id)
        val model: UserModel
        if (result == null) {
            model = createUser(id, username)
            return repository.create(model)
        }
        model = result.copy(username = username)
        repository.update(id, model)
    }

    @Throws(EntryNotFoundException::class)
    public suspend fun changeBalance(id: Long, newBalance: Long) {
        val result = getUser(id)
        val model = result.copy(balance = newBalance)
        repository.update(id, model)
    }

    @Throws(EntryNotFoundException::class)
    public suspend fun incrementWins(id: Long) {
        val result = getUser(id)
        val model = result.copy(wins = result.wins.inc())
        repository.update(id, model)
    }

    @Throws(EntryNotFoundException::class)
    public suspend fun decrementWins(id: Long) {
        val result = getUser(id)
        val model = result.copy(wins = result.wins.dec())
        repository.update(id, model)
    }

    @Throws(EntryNotFoundException::class)
    public suspend fun incrementLosses(id: Long) {
        val result = getUser(id)
        val model = result.copy(losses = result.losses.inc())
        repository.update(id, model)
    }

    @Throws(EntryNotFoundException::class)
    public suspend fun decrementLosses(id: Long) {
        val result = getUser(id)
        val model = result.copy(losses = result.losses.dec())
        repository.update(id, model)
    }

    private suspend fun createUser(id: Long, username: String, balance: Long = 0, wins: Long = 0, losses: Long = 0): UserModel {
        val model = UserModel(id, username, balance, wins, losses)
        repository.create(model)
        return model
    }

    @Throws(EntryNotFoundException::class)
    private suspend fun getUser(id: Long): UserModel {
        return repository.read(id) ?: throw EntryNotFoundException("The user with id `$id` not found")
    }

    @Throws(EntryNotFoundException::class)
    private suspend fun getUser(username: String): UserModel {
        return repository.read(username) ?: throw EntryNotFoundException("The user with username `$username` not found")
    }
}