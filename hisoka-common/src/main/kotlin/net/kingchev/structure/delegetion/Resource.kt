package net.kingchev.structure.delegetion

import kotlinx.io.files.FileNotFoundException
import java.util.Properties
import kotlin.reflect.KProperty

public class Property(private val value: String) {
    private val properties: Properties = Properties()

    init {
        properties.load(javaClass.getResourceAsStream("/application.properties") ?: throw FileNotFoundException("Properties file is not defined"))
    }

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        return properties.getProperty(value)
    }
}

public class Environment(private val value: String) {
    public operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return System.getenv(value)
    }
}