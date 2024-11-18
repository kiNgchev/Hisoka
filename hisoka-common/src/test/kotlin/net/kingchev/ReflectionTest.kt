package net.kingchev

import net.kingchev.utils.ReflectionUtils
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ReflectionTest {
    @Test
    fun getClassReflectionTest() {
        assertEquals(ReflectionUtils.getSubclasses(klass = Base::class), listOf(Subclass1::class, Subclass2::class, Subclass3::class, Subclass5::class))
    }
}

open class Base
class Subclass1 : Base()
class Subclass2 : Base()
open class Subclass3 : Base()
class Subclass4
class Subclass5 : Subclass3()