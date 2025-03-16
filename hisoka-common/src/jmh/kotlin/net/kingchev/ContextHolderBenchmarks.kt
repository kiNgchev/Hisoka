package net.kingchev

import net.kingchev.context.ContextHolder

class ContextHolderBenchmarks {
    private val holder = ContextHolder

    init {
        holder["test"] = TestWrapper(1)
        holder["test2"] = TestWrapper(10)
    }

    fun getEntryBenchmark(): TestWrapper {
        return holder["test"]
    }

    fun setEntryBenchmark() {
        holder["test3"] = TestWrapper(11)
    }

    fun removeEntryBenchmark() {
        holder -= "test2"
    }
}