package net.kingchev.service

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import net.kingchev.structure.Initializable
import org.slf4j.LoggerFactory

public object DotenvConfigService : Initializable {
    private val logger = LoggerFactory.getLogger(DotenvConfigService::class.java)
    public override val order: Int = 0

    public lateinit var dotenv: Dotenv
        private set

    override suspend fun initialize() {
        logger.info("Dotenv initialization started")
        dotenv = dotenv {
            ignoreIfMalformed = true
            ignoreIfMissing = true
        }
        logger.info("Dotenv initialization completed")
    }
}