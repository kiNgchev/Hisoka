package net.kingchev.localization.service

import net.kingchev.localization.model.Bundle
import net.kingchev.localization.model.Language
import java.util.*

public object LocaleService {
    private val bundles: MutableMap<String, Array<Bundle>> = mutableMapOf()

    init {
        createBundle("common")
        // test bundle, only for test
        createBundle("test")
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

    private fun createBundle(baseName: String) {
        for (lang in Language.entries) {
            val bundle = Bundle(baseName, lang)
            bundles[lang.language]?.plus(bundle) ?:
            bundles.set(lang.language, arrayOf(bundle))
        }
    }
}