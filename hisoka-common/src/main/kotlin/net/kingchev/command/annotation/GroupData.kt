package net.kingchev.command.annotation

import java.lang.annotation.Inherited

@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(allowedTargets = [AnnotationTarget.CLASS])
public annotation class GroupData(
    public val name: String,
    public val description: String
)