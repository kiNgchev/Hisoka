package net.kingchev

import net.kingchev.structure.delegation.Environment
import net.kingchev.structure.delegation.Property
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DelegationTest {
    @Test
    fun delegationTest() {
        val e by Environment("test")
        val p by Property("test")
        assertEquals(e, "test")
        assertEquals(p, "test")
    }
}