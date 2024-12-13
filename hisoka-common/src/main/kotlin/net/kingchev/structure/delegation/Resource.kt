package net.kingchev.structure.delegation

import io.github.cdimascio.dotenv.dotenv
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
            properties.load(javaClass.getResourceAsStream(path) ?: throw FileNotFoundException("Properties file not found on path: $path"))
        } else properties = System.getProperties();
    }

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return properties.getProperty(key) ?: throw NullPointerException("This property value is not defined for $key")
    }
}

public class Environment(private val key: String, private val system: Boolean = true) {
    private fun getSystem(key: String): String? {
        return System.getenv(key)
    }

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return if (system) getSystem(key)
        else {
            DotenvConfigService.dotenv[key] ?: getSystem(key)
        } ?: throw NullPointerException("This environment value is not defined for $key")
    }
}