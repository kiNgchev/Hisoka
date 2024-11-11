package net.kingchev.service

import net.kingchev.extensions.toIntOrDef
import net.kingchev.model.HisokaConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public object ConfigService {
    private val logger: Logger = LoggerFactory.getLogger(ConfigService::class.java)

    public val config: HisokaConfig

    init {
        logger.info("Config initialization started")

        val token = System.getenv("BOT_TOKEN")
        val totalShards = System.getenv("TOTAL_SHARDS") ?: System.getProperty("total.shards", "1")

        config = HisokaConfig(token = token, totalShards = totalShards.toIntOrDef())
        logger.info("Config initialization completed")
    }
}