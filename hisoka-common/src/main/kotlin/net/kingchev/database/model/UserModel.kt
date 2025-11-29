package net.kingchev.database.model

import net.kingchev.localization.Language

public data class UserModel(
    public val id: Long,
    public val username: String,
    public val isPremium: Boolean,
    public val locale: Language
)