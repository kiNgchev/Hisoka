package net.kingchev.database.service

import net.kingchev.database.exception.EntryNotFoundException
import net.kingchev.database.model.UserModel
import net.kingchev.database.repository.UserRepository
import net.kingchev.localization.model.Language
import net.kingchev.localization.model.parse

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
    public suspend fun setLocale(id: Long, locale: String) {
        setLocale(id, parse(locale))
    }

    @Throws(EntryNotFoundException::class)
    public suspend fun setLocale(id: Long, locale: Language) {
        val result = getUser(id)
        val model = result.copy(locale = locale.language)
        repository.update(id, model)
    }

    @Throws(EntryNotFoundException::class)
    public suspend fun getLocale(id: Long): String {
        return getUser(id).locale
    }

    public suspend fun getLocale(id: Long, username: String): String {
        return getUserWithCreate(id, username).locale
    }

    public suspend fun createUser(id: Long, username: String, isPremium: Boolean = false, locale: Language = Language.EN_US): UserModel {
        val model = UserModel(id, username, isPremium, locale.language)
        repository.create(model)
        return model
    }

    @Throws(EntryNotFoundException::class)
    private suspend fun getUser(id: Long): UserModel {
        return repository.read(id) ?: throw EntryNotFoundException("The user with id `$id` not found")
    }

    private suspend fun getUserWithCreate(id: Long, username: String): UserModel {
        return repository.read(id) ?: createUser(id, username)
    }

    @Throws(EntryNotFoundException::class)
    private suspend fun getUser(username: String): UserModel {
        return repository.read(username) ?: throw EntryNotFoundException("The user with username `$username` not found")
    }
}