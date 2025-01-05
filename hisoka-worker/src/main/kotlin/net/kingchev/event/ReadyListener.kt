package net.kingchev.event

import dev.kord.common.entity.PresenceStatus
import dev.kord.core.Kord
import dev.kord.core.entity.Emoji
import dev.kord.core.entity.StandardEmoji
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.on
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class ReadyListener(kord: Kord) : IListener {
    private val logger: Logger = LoggerFactory.getLogger(ReadyListener::class.java)

    init {
        kord.on<ReadyEvent> {
            kord.editPresence {
                status = PresenceStatus.DoNotDisturb
                playing("Покер\uD83C\uDCCF")
            }
            logger.info("Bot was started with id[${kord.selfId}], name[${kord.getSelf().username}]")
        }
    }
}