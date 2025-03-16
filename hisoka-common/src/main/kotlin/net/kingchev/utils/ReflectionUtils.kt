package net.kingchev.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.lang.Thread.currentThread
import java.net.URLDecoder
import java.nio.file.Paths
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

public const val BASE_APP_PACKAGE: String = "net.kingchev"

public object ReflectionUtils {
    public val logger: Logger = LoggerFactory.getLogger(this::class.java)

    public inline fun getClass(packageName: String = BASE_APP_PACKAGE, block: (klass: KClass<*>) -> Boolean): HashSet<KClass<*>> {
        val result = hashSetOf<KClass<*>>()
        val loader = currentThread().contextClassLoader
        val path = packageName.replace(".", File.separator)

        val tmp = loader.getResource(path) ?: return result
        val currDir = File(tmp.path)

        currDir.walkTopDown()
            .filter { !it.equals(null) and it.isFile and it.extension.equals("class", true) }
            .forEach { file ->
                try {
                    val relativePath = currDir.toPath()
                        .relativize(file.toPath())
                        .toString()
                    val name = "$packageName." + relativePath
                        .replace(File.separator, ".")
                        .removeSuffix(".class")

                    val clazz = loader.loadClass(name).kotlin
                    if (block.invoke(clazz))
                        result.add(clazz)
                } catch (_: NoClassDefFoundError) {
                    logger.error("We have found class [$file], and couldn't load it.")
                } catch (_: ClassNotFoundException) {
                    logger.error("We could not find class [$file]")
                }
            }
        return result
    }

    public fun getSubclasses(packageName: String = BASE_APP_PACKAGE, klass: KClass<*>): HashSet<KClass<*>> {
        return getClass(packageName) { isSuperclassContains(it, klass) }
    }

    @Suppress("UNCHECKED_CAST")
    public inline fun <reified T : Any> getSubclasses(packageName: String = BASE_APP_PACKAGE): HashSet<KClass<T>> {
        return getClass(packageName) { isSuperclassContains(it, T::class) }.map { it as KClass<T> }.toHashSet()
    }

    public fun isSuperclassContains(klass: KClass<*>, superKlass: KClass<*>): Boolean {
        if (klass.superclasses.contains(superKlass)) return true
        else for (cls in klass.superclasses) {
            if (isSuperclassContains(cls, superKlass)) return true
        }
        return false
    }
}
