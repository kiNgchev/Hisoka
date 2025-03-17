package net.kingchev

import net.kingchev.context.ContextHolder
import org.openjdk.jmh.annotations.Benchmark

open class ContextHolderBenchmarks {
    private val holder = ContextHolder

    init {
        holder["test"] = TestWrapper(1)
        holder["test2"] = TestWrapper(10)
    }

    @Benchmark
    fun getEntryBenchmark(): TestWrapper {
        return holder["test"]
    }

    @Benchmark
    fun setEntryBenchmark() {
        holder["test3"] = TestWrapper(11)
    }

    @Benchmark
    fun removeEntryBenchmark() {
        holder -= "test2"
    }
}