package net.kingchev

import net.kingchev.context.ContextHolder
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ContextHolderTest {
    private val contextHolder = ContextHolder

    @Test
    fun setEntry() {
        contextHolder["test2"] = TestWrapper(10)
    }

    @Test
    fun getEntry() {
        assertEquals(contextHolder.get<TestWrapper>("test2").value, 10)
    }

    @Test
    fun removeEntry() {
        contextHolder -= "test2"
    }
}

class TestWrapper(
    val value: Int = 0
)