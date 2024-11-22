package net.kingchev.database.model

public class UserModel(
    public val id: Long,
    public val username: String,
    public val balance: Long,
    public val wins: Long,
    public val losses: Long,
)