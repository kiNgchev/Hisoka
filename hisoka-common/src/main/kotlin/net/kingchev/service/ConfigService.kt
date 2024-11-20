package net.kingchev.service

import net.kingchev.extensions.toIntOrDef
import net.kingchev.model.HisokaConfig
import net.kingchev.structure.Initializable
import net.kingchev.structure.delegetion.Environment
import net.kingchev.structure.delegetion.Property
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public object ConfigService : Initializable {
    private val logger: Logger = LoggerFactory.getLogger(ConfigService::class.java)

    public var config: HisokaConfig? = null

    public override fun initialize() {
        logger.info("Config initialization started")

        val token by Environment("BOT_TOKEN")
        val totalShards by Property("shards.total")

        config = HisokaConfig(token = token, totalShards = totalShards.toIntOrDef(1))
        logger.info("Config initialization completed")
    }
}