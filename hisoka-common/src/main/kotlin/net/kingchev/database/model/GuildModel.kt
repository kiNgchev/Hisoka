package net.kingchev.database.model

public data class GuildModel(
    public val id: Long,
    public val name: String,
    public val description: String?,
    public val memberCount: Int,
    public val roles: List<Long>,
    public val isPremium: Boolean,
)