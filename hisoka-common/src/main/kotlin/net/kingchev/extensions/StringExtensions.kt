package net.kingchev.extensions

public fun Any?.toByteOrDef(definition: Byte = 0): Byte {
    return try {
        java.lang.Byte.parseByte(this.toString())
    } catch (_: NumberFormatException) { definition }
}

public fun Any?.toShortOrDef(definition: Short = 0): Short {
    return try {
        java.lang.Short.parseShort(this.toString())
    } catch (_: NumberFormatException) { definition }
}

public fun Any?.toIntOrDef(definition: Int = 0): Int {
    return try {
        java.lang.Integer.parseInt(this.toString())
    } catch (_: NumberFormatException) { definition }
}

public fun Any?.toLongOrDef(definition: Long = 0): Long {
    return try {
        java.lang.Long.parseLong(this.toString())
    } catch (_: NumberFormatException) { definition }
}

public fun Any?.toFloatOrDef(definition: Float = 0f): Float {
    return try {
        java.lang.Float.parseFloat(this.toString())
    } catch (_: NumberFormatException) { definition }
}
