package net.kingchev.service

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import net.kingchev.structure.Initializable

public object DotenvConfigService : Initializable {
    public lateinit var dotenv: Dotenv
        private set

    override suspend fun initialize() {
        dotenv = dotenv {
            ignoreIfMalformed = true
            ignoreIfMissing = true
        }
    }
}