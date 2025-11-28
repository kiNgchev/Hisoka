package net.kingchev.service

import net.kingchev.extensions.toIntOrDef
import net.kingchev.model.HisokaConfig
import net.kingchev.structure.Initializable
import net.kingchev.structure.delegation.env
import net.kingchev.structure.delegation.property
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public object ConfigService : Initializable {
    private val logger: Logger = LoggerFactory.getLogger(ConfigService::class.java)
    public override val order: Int = 1

    public lateinit var config: HisokaConfig
        private set

    public override suspend fun initialize() {
        logger.info("Config initialization started")

        val token: String by env("BOT_TOKEN")
        val totalShards: Int by property("shards.total") { it.toIntOrDef(1)}

        config = HisokaConfig(token = token, totalShards = totalShards)
        logger.info("Config initialization completed")
    }
}