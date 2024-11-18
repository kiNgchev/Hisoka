package net.kingchev.structure

public class InitializeException : Exception {
    public constructor()
            : super()

    public constructor(message: String?)
            : super(message)

    public constructor(message: String?, cause: Throwable?)
            : super(message, cause)

    public constructor(cause: Throwable?)
            : super(cause)

    public constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean)
            : super(message, cause, enableSuppression, writableStackTrace)
}