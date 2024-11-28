package net.kingchev.extensions

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.Channel

public val User.idLong: Long get() = Convertor.toLong(this.id)

public val Member.idLong: Long get() = Convertor.toLong(this.id)

public val Channel.idLong: Long get() = Convertor.toLong(this.id)

public val Guild.idLong: Long get() = Convertor.toLong(this.id)

private object Convertor {
    fun toLong(id: Snowflake): Long = id.value.toLong()
}
