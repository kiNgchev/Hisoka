package net.kingchev

import net.kingchev.utils.ReflectionUtils
import org.openjdk.jmh.annotations.Benchmark
import kotlin.reflect.KClass

open class ReflectionBenchmarks {
    @Benchmark
    fun getSubclassesBenchmark(): HashSet<KClass<Base>> {
        return ReflectionUtils.getSubclasses<Base>()
    }
}