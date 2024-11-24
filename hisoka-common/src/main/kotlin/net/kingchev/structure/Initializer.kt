package net.kingchev.structure

import net.kingchev.utils.ReflectionUtils

public object Initializer {
    public fun init() {
        val initializables = ReflectionUtils.getSubclasses<Initializable>()
        for (initializable in initializables) {
            try {
                val instance = initializable.objectInstance
                instance?.initialize()
            } catch (e: Exception) {
                throw InitializeException(e.message, e.cause)
            }
        }
    }
}