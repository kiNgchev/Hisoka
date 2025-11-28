package net.kingchev.structure

import net.kingchev.utils.ReflectionUtils

public object Initializer {
    public suspend fun init() {
        val initializables = ReflectionUtils.getSubclasses<Initializable>()
        initializables
            .map { initializable ->
                try {
                    initializable.objectInstance
                } catch (e: Exception) {
                    throw InitializeException(e.message, e.cause)
                }
            }
            .sortedBy { it?.order }
            .forEach { initializable -> initializable?.initialize() }
    }
}