package net.kingchev.structure

public interface Initializable {
    @Throws(InitializeException::class)
    public fun initialize()
}