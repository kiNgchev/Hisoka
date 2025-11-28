package net.kingchev.structure

public interface Initializable {
    public val order: Int
        get() = 0

    @Throws(InitializeException::class)
    public suspend fun initialize()
}