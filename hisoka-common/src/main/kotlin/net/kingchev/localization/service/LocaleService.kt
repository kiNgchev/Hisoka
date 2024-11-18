package net.kingchev.localization.service

import net.kingchev.localization.model.Bundle
import net.kingchev.localization.model.Language
import java.util.*

public object LocaleService {
    private val bundles: MutableMap<String, Array<Bundle>> = mutableMapOf()

    init {
        createBundle("common")
    }

    public fun getMessage(key: String, locale: String): String {
        bundles[locale]?.forEach {
            try {
                val result = it.entries.getString(key)
                return result
            } catch (_: MissingResourceException) {
                return@forEach
            }
        }
        return key
    }

    public fun getMessage(key: String, locale: String, vararg objects: Any): String {
        return getMessage(key, locale).format(*objects)
    }

    public fun createBundle(baseName: String) {
        for (lang in Language.entries) {
            val bundle = Bundle(baseName, lang)
            val array = bundles[lang.language]
            if (array == null) bundles[lang.language] = arrayOf(bundle)
            else bundles[lang.language] = array.plusElement(bundle)
        }
    }
}