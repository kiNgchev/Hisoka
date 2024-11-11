package net.kingchev.command

import java.lang.annotation.Inherited

@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(allowedTargets = [AnnotationTarget.CLASS])
public annotation class CommandData(
    val key: String,
    val aliases: Array<String> = [],
    val description: String,
    val group: String,
    val hidden: Boolean = false,
    val developerOnly: Boolean = false,
    val guildOnly: Boolean = true,
    val dmOnly: Boolean = false
)
