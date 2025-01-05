package net.kingchev.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.lang.Thread.currentThread
import java.net.URLDecoder
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

public const val BASE_APP_PACKAGE: String = "net.kingchev"

public object ReflectionUtils {
    public val logger: Logger = LoggerFactory.getLogger(this::class.java)

    public fun getClass(packageName: String = BASE_APP_PACKAGE, block: (klass: KClass<*>) -> Boolean): HashSet<KClass<*>> {
        val result = hashSetOf<KClass<*>>()
        val loader = currentThread().contextClassLoader
        val path = packageName.replace(".", "/")

        try {
            val res = loader.getResources(path)
            while (res.hasMoreElements()) {
                val dirPath = URLDecoder.decode(res.nextElement().path, "UTF-8")
                val dir = File(dirPath)
                dir.listFiles()?.forEach { file ->
                    if (file.isDirectory) result.addAll(getClass("$packageName.${file.name}", block))
                }
            }
        } catch (_: IOException) {
            logger.error("Failed to load resources for [$packageName]")
        }
        val tmp = loader.getResource(path) ?: return result

        val currDir = File(tmp.path)
        currDir.list()?.forEach { classFile ->
            if (classFile.endsWith(".class")) {
                try {
                    val add: KClass<*> = Class.forName("$packageName.${classFile.substring(0, classFile.length - 6)}").kotlin
                    if (block.invoke(add)) result.add(add)
                } catch (_: NoClassDefFoundError) {
                    logger.error("We have found class [$classFile], and couldn't load it.")
                } catch (_: ClassNotFoundException) {
                    logger.error("We could not find class [$classFile]")
                }
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
