package net.kingchev.localization.service

import io.ktor.utils.io.core.*
import net.kingchev.localization.model.Bundle
import net.kingchev.localization.model.Language
import java.nio.charset.Charset
import java.util.*
import kotlin.text.String
import kotlin.text.toByteArray

public object LocaleService {
    private val bundles: MutableMap<String, MutableList<Bundle>> = mutableMapOf()

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
            bundles[lang.language]?.add(bundle) ?:
            bundles.set(lang.language, mutableListOf(bundle))
        }
    }
}