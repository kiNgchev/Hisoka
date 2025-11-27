package net.kingchev.utils


public class MapBuilder<K, V> {
    private val map = mutableMapOf<K, V>()

    public infix fun K.to(value: V): V? = map.put(this, value)

    public fun build(): Map<K, V> = map.toMap()
}

public fun <K, V> buildMap(block: MapBuilder<K, V>.() -> Unit): MutableMap<K, V> {
    val builder = MapBuilder<K, V>().apply(block)
    val result = mutableMapOf<K, V>()
    result.putAll(builder.build())

    return result
}