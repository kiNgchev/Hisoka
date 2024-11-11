package net.kingchev.extensions

public fun String.toByteOrDef(definition: Byte = 0): Byte {
    return try {
        java.lang.Byte.parseByte(this)
    } catch (_: NumberFormatException) { definition }
}

public fun String.toShortOrDef(definition: Short = 0): Short {
    return try {
        java.lang.Short.parseShort(this)
    } catch (_: NumberFormatException) { definition }
}

public fun String.toIntOrDef(definition: Int = 0): Int {
    return try {
        java.lang.Integer.parseInt(this)
    } catch (_: NumberFormatException) { definition }
}

public fun String.toLongOrDef(definition: Long = 0): Long {
    return try {
        java.lang.Long.parseLong(this)
    } catch (_: NumberFormatException) { definition }
}

public fun String.toFloatOrDef(definition: Float = 0f): Float {
    return try {
        java.lang.Float.parseFloat(this)
    } catch (_: NumberFormatException) { definition }
}
