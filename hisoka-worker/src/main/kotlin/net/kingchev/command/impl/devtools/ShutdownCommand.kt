package net.kingchev.command.impl.devtools

import dev.kord.core.Kord
import dev.kord.core.behavior.reply
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import net.kingchev.dsl.event.IListener
import net.kingchev.extensions.idLong
import net.kingchev.model.BotMetadata
import kotlin.system.exitProcess

public class ShutdownCommand(kord: Kord) : IListener {
    init {
        kord.on<MessageCreateEvent> {
            if (this.message.content != "h.shutdown")
                return@on
            if (!BotMetadata.DEVELOPERS.contains(this.message.author?.idLong))
                return@on
            val rm = message.reply { content = "Выключаю бота..." }.referencedMessage
            //TODO: add graceful shutdown, if needed
            rm?.addReaction(ReactionEmoji.Unicode("✅"))
            exitProcess(0)
        }
    }
}