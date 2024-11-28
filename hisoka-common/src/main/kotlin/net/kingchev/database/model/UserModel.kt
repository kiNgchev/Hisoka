package net.kingchev.database.model

public data class UserModel(
    public val id: Long,
    public val username: String,
    public val balance: Long,
    public val wins: Long,
    public val losses: Long,
    public val locale: String
)