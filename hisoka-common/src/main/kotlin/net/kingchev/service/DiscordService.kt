package net.kingchev.service

import dev.kord.core.Kord
import dev.kord.gateway.Intent
import dev.kord.gateway.builder.Shards
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class DiscordService {
    private val bot: BotService = BotService

    private val config = ConfigService.config

    private lateinit var kord: Kord

    public suspend fun login() {
        kord = Kord(config!!.token) {
            sharding {
                Shards(totalShards = config.totalShards)
            }
        }

        bot.registerEvents(kord)
        bot.registerCommands(kord)
        bot.registerGroupCommands(kord)

        kord.login() {
            Intent.entries.forEach { intents += it }
            logger.info("Bot was started")
        }
    }

    public companion object {
        public val logger: Logger = LoggerFactory.getLogger(DiscordService::class.java)
    }
}