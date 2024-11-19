package net.kingchev.service

import net.kingchev.extensions.toIntOrDef
import net.kingchev.model.HisokaConfig
import net.kingchev.structure.Initializable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public object ConfigService : Initializable {
    private val logger: Logger = LoggerFactory.getLogger(ConfigService::class.java)

    public var config: HisokaConfig? = null

    public override fun initialize() {
        logger.info("Config initialization started")

        val token = System.getenv("BOT_TOKEN")
        val totalShards = System.getenv("TOTAL_SHARDS") ?: System.getProperty("total.shards", "1")

        config = HisokaConfig(token = token, totalShards = totalShards.toIntOrDef())
        logger.info("Config initialization completed")
    }
}