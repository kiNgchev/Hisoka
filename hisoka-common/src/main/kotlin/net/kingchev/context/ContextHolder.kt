package net.kingchev.context

import java.util.concurrent.ConcurrentHashMap

public object ContextHolder {
    public val lock: Any = Any()
    public val entries: ConcurrentHashMap<String, Any> = ConcurrentHashMap()

    @Suppress("UNCHECKED_CAST")
    public inline operator fun <reified T> get(key: String): T = try {
        synchronized(lock) { entries[key] as? T }
    } catch (_: ClassCastException) {
        throw IllegalArgumentException("Entry value is not possible cast to ${T::class}.")
    } catch (_: TypeCastException) {
        throw IllegalArgumentException("Entry value is not possible cast to ${T::class}.")
    } ?: throw IllegalArgumentException("Entry with key $key not found.")

    public operator fun <T> set(key: String, entry: T): T {
        synchronized(lock) { entries[key] = entry as Any }
        return entry
    }

    public operator fun minusAssign(name: String) {
        synchronized(lock) { entries -= name }
    }
}