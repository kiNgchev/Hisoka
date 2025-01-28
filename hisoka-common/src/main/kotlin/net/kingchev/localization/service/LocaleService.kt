package net.kingchev.localization.service

import dev.kord.common.Locale
import net.kingchev.localization.model.Bundle
import net.kingchev.localization.model.DEFAULT_LOCALE
import net.kingchev.localization.model.Language
import java.util.*

public object LocaleService {
    private val bundles: MutableMap<String, Array<Bundle>> = mutableMapOf()
    private val discordLocales: Map<Locale, Language> = mapOf(
        Locale.GERMAN to Language.DE_DE,
        Locale.RUSSIAN to Language.RU_RU
    )

    init {
        createBundle("common")
        createBundle("messages")
    }

    public fun getMessage(key: String, locale: String): String {
        bundles[locale]?.forEach {
            try {
                val result = it.entries.getString(key)
                return String(result.toByteArray(), charset("UTF-8"))
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

    public fun createDiscordMessage(key: String): MutableMap<Locale, String>
        = discordLocales
            .map { it.key to getMessage(key, it.value.language) }
            .toMap() as MutableMap
}

public fun getMessage(key: String, locale: Language): String = LocaleService.getMessage(key, locale.language)
public fun getMessage(key: String, locale: String): String = LocaleService.getMessage(key, locale)

public fun getMessage(key: String, locale: Language, vararg objects: Any): String = LocaleService.getMessage(key, locale.language, *objects)
public fun getMessage(key: String, locale: String, vararg objects: Any): String = LocaleService.getMessage(key, locale, *objects)
