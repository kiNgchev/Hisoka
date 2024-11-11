package net.kingchev

import net.kingchev.extensions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StringExtensionTest {
    @Test
    fun testStringExtension() {
        assertEquals("1".toByteOrDef(0), 1)
        assertEquals("e".toByteOrDef(0), 0)
        assertEquals("1".toShortOrDef(0), 1)
        assertEquals("e".toShortOrDef(0), 0)
        assertEquals("1".toIntOrDef(0), 1)
        assertEquals("e".toIntOrDef(0), 0)
        assertEquals("1".toLongOrDef(0), 1)
        assertEquals("e".toLongOrDef(0), 0)
        assertEquals("1".toFloatOrDef(0f), 1f)
        assertEquals("e".toFloatOrDef(0f), 0f)
    }
}