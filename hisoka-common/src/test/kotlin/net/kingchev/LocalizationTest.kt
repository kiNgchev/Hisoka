package net.kingchev

import net.kingchev.localization.LocaleService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LocalizationTest {
    @Test
    fun stringCompareTest() {
        LocaleService.createBundle("test")
        assertEquals(LocaleService.getMessage("good.morning", "en"), "Good Morning!")
        assertEquals(LocaleService.getMessage("good.morning", "ru"), "Доброе утро!")
    }
}