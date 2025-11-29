package net.kingchev.context

public data class ContextEntry<V>(
    val key: String,
    val value: V,
)