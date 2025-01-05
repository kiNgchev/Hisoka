package net.kingchev.database.model

public data class UserModel(
    public val id: Long,
    public val username: String,
    public val isPremium: Boolean,
    public val locale: String
)