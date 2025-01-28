@file:Suppress("UNUSED")

package net.kingchev.structure.delegation

import net.kingchev.localization.model.DEFAULT_LOCALE
import net.kingchev.localization.model.Language
import net.kingchev.localization.model.parse
import net.kingchev.localization.service.getMessage
import kotlin.reflect.KProperty

public class Locale(private val key: String, private val locale: Language) {
    public constructor(key: String, locale: String) : this(key, parse(locale))

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return getMessage(key, locale)
    }

    public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String): Nothing
            = throw UnsupportedOperationException("You may not change this value")
}

public fun message(key: String): Locale = Locale(key, DEFAULT_LOCALE)

public fun message(key: String, locale: Language): Locale = Locale(key, locale)

public fun message(key: String, locale: String): Locale = Locale(key, locale)