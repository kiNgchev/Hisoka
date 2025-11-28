package net.kingchev

import net.kingchev.structure.delegation.env
import net.kingchev.structure.delegation.property
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DelegationTest {
    @Test
    fun delegationTest() {
        val e: String by env("test")
        val p: String by property("test")
        assertEquals(e, "test")
        assertEquals(p, "test")
    }
}