package net.kingchev

import net.kingchev.utils.ReflectionUtils
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ReflectionTest {
    @Test
    fun getClassReflectionTest() {
        val actual = setOf(Subclass1::class, Subclass2::class, Subclass3::class, Subclass5::class)
        assertEquals(ReflectionUtils.getSubclasses<TestBase>().toSet(), actual)
        assertEquals(ReflectionUtils.getSubclasses(klass = TestBase::class).toSet(), actual)
    }
}

open class TestBase
class Subclass1 : TestBase()
class Subclass2 : TestBase()
open class Subclass3 : TestBase()
class Subclass4
class Subclass5 : Subclass3()