package net.kingchev.localization.model

import net.kingchev.localization.model.Language.EN_US
import java.util.*

public class Bundle(baseName: String, language: Language) {
    public val entries: ResourceBundle = ResourceBundle.getBundle("locale.$baseName", language.locale)
}

public enum class Language(public val language: String, country: String, nativeName: String, englishName: String) {
    DE_DE("de", "DE", "Deutsch", "German"),
    EN_US("en", "US", "English", "English"),
    RU_RU("ru", "RU", "Русский", "Russian");

    public var code: String = language + "_" + country
    public var locale: Locale = Locale.of(language, country)
    public var nativeName: String? = nativeName
    public var englishName: String? = englishName
}

public fun parse(value: String): Language {
    for (language in Language.entries) {
        if (language.name.equals(value, ignoreCase = true)
            || language.code.equals(value, ignoreCase = true)
            || language.nativeName.equals(value, ignoreCase = true)
            || language.englishName.equals(value, ignoreCase = true)
        ) return language
    }

    return EN_US
}