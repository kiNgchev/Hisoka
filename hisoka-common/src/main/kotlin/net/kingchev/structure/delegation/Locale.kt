@file:Suppress("UNUSED")

package net.kingchev.structure.delegation

import net.kingchev.localization.DEFAULT_LOCALE
import net.kingchev.localization.Language
import net.kingchev.localization.getMessage
import net.kingchev.localization.parse
import kotlin.reflect.KProperty

public class Locale(private val key: String, private val locale: Language) {
    public constructor(key: String, locale: String) : this(key, parse(locale))

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): String = getMessage(key, locale)

    public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String): Nothing
        = throw UnsupportedOperationException("You may not change this value")
}

public fun i18n(key: String): Locale = Locale(key, DEFAULT_LOCALE)

public fun i18n(key: String, locale: Language): Locale = Locale(key, locale)

public fun i18n(key: String, locale: String): Locale = Locale(key, locale)