@file:Suppress("UNUSED")

package net.kingchev.structure.delegation

import kotlinx.io.files.FileNotFoundException
import net.kingchev.service.DotenvConfigService
import java.util.*
import kotlin.reflect.KProperty

public class Property(private val key: String, private var path: String = "application.properties", system: Boolean = false) {
    private val properties: Properties

    init {
        if (!system) {
            if (!path.startsWith("/"))
                path = "/$path"
            properties = Properties()
            properties.load(javaClass.getResourceAsStream(path)
                ?: throw FileNotFoundException("Properties file not found on path: $path"))
        } else properties = System.getProperties()
    }

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): String
        = properties.getProperty(key)
        ?: throw NullPointerException("This property value is not defined for $key")

    public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String): Nothing
            = throw UnsupportedOperationException("You may not change this value")
}

public class Environment(private val key: String, private val system: Boolean = true) {
    private fun getSystem(key: String): String? {
        return System.getenv(key)
    }

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return if (system) {
            getSystem(key)
        } else {
            DotenvConfigService.dotenv[key] ?: getSystem(key)
        } ?: throw NullPointerException("This environment value is not defined for $key")
    }

    public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String): Nothing
        = throw UnsupportedOperationException("You may not change this value")
}

public fun property(key: String, path: String, system: Boolean = false): Property = Property(key, path, system)
public fun property(key: String, system: Boolean = false): Property = Property(key, system = system)

public fun env(key: String, system: Boolean = true): Environment = Environment(key, system)