package net.kingchev.structure.delegation

import kotlinx.io.files.FileNotFoundException
import java.util.*
import kotlin.reflect.KProperty

public class Property(private val key: String, private var path: String = "application.properties", system: Boolean = false) {
    private val properties: Properties

    init {
        if (!system) {
            if (!path.startsWith("/"))
                path = "/$path"
            properties = Properties()
            properties.load(javaClass.getResourceAsStream(path) ?: throw FileNotFoundException("Properties file not found on path: $path"))
        } else properties = System.getProperties();
    }

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return properties.getProperty(key) ?: throw NullPointerException("This property value is not defined for $key")
    }
}

public class Environment(private val key: String, private val system: Boolean = true) {
    public operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return if (system) System.getenv(key) ?: throw NullPointerException("This environment value is not defined for $key")
        else throw UnsupportedOperationException("Unable to get environment variables from .env file")
    }
}