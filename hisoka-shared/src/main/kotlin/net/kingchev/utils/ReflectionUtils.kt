package net.kingchev.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.Thread.currentThread
import java.net.URL
import java.util.jar.JarFile
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.superclasses

public const val BASE_APP_PACKAGE: String = "net.kingchev"

public object ReflectionUtils {
    public val logger: Logger = LoggerFactory.getLogger(this::class.java)

    public inline fun collect(vararg packages: String, crossinline block: (klass: KClass<*>) -> Boolean): HashSet<KClass<*>>
        = packages.fold(hashSetOf()) { set, packageName ->
            set.addAll(collect(packageName, block)); set
        }

    public inline fun collect(packageName: String = BASE_APP_PACKAGE, crossinline block: (klass: KClass<*>) -> Boolean): HashSet<KClass<*>> {
        val result = hashSetOf<KClass<*>>()
        val loader = currentThread().contextClassLoader
        val path = packageName.replace(".", "/")

        val roots = loader.getResources(path) ?: return result

        roots.toList().stream()
            .parallel()
            .forEach { root ->
                result += collect(loader, packageName, root, block)
            }

        return result
    }

    public inline fun collect(loader: ClassLoader, packageName: String, root: URL, crossinline block: (klass: KClass<*>) -> Boolean): HashSet<KClass<*>> {
        val currDir = File(root.path)

        if (root.protocol == "jar") {
            val path = root.path.split("!")
            val jarPath = path[0].replace("file:/", "")
            return collect(loader, packageName, JarFile(jarPath), block)
        }

        val classes = currDir.walkTopDown()
            .filter { it.isFile and it.extension.equals("class", true) }
            .map { file ->
                val relativePath = currDir.toPath()
                    .relativize(file.toPath())
                    .toString()
                val name = "$packageName.$relativePath"
                    .replace(File.separator, ".")
                    .removeSuffix(".class")
                name
            }
            .toList()
        return loadClasses(loader, classes, block)
    }

    public inline fun collect(loader: ClassLoader, packageName: String, root: JarFile, crossinline block: (klass: KClass<*>) -> Boolean): HashSet<KClass<*>> {
        val classes = root.stream()
            .toList()
            .filter { it.name.endsWith(".class") }
            .mapNotNull { file ->
                val name = file.name
                    .replace("/", ".")
                    .removeSuffix(".class")

                if (!name.startsWith(packageName))
                    return@mapNotNull null
                name
            }
        return loadClasses(loader, classes, block)
    }

    public inline fun loadClasses(loader: ClassLoader, classes: List<String>, crossinline block: (klass: KClass<*>) -> Boolean): HashSet<KClass<*>>
        = classes
            .mapNotNull {
                try {
                    loader.loadClass(it).kotlin
                } catch (_: NoClassDefFoundError) {
                    logger.error("We have found class [$it], and couldn't load it.")
                    return@mapNotNull null
                } catch (_: ClassNotFoundException) {
                    logger.error("We could not find class [$it]")
                    return@mapNotNull null
                }
            }
            .filter { block(it) }
            .toHashSet()

    public fun getSubclasses(packageName: String = BASE_APP_PACKAGE, klass: KClass<*>): HashSet<KClass<*>> {
        return collect(packageName) { isSuperclassContains(it, klass) }
    }

    @Suppress("UNCHECKED_CAST")
    public inline fun <reified T : Any> getSubclasses(packageName: String = BASE_APP_PACKAGE): HashSet<KClass<T>> {
        return collect(packageName) { isSuperclassContains(it, T::class) }
            .map { it as KClass<T> }
            .toHashSet()
    }

    @Suppress("UNCHECKED_CAST")
    public inline fun <reified T : Any> getSubclasses(vararg packages: String = arrayOf(BASE_APP_PACKAGE)): HashSet<KClass<T>> {
        return collect(*packages) { isSuperclassContains(it, T::class) }
            .map { it as KClass<T> }
            .toHashSet()
    }

    public inline fun <reified A : Annotation> hasAnnotation(packageName: String = BASE_APP_PACKAGE): HashSet<KClass<*>> {
        return collect(packageName) { isAnnotationContains<A>(it) }
    }

    public inline fun <reified A : Annotation> hasAnnotation(vararg packages: String = arrayOf(BASE_APP_PACKAGE)): HashSet<KClass<*>> {
        return collect(*packages) { isAnnotationContains<A>(it) }
    }


    public fun isSuperclassContains(klass: KClass<*>, superKlass: KClass<*>): Boolean {
        if (klass.superclasses.contains(superKlass)) return true
        else for (cls in klass.superclasses) {
            if (isSuperclassContains(cls, superKlass)) return true
        }
        return false
    }

    public inline fun <reified A : Annotation> isAnnotationContains(klass: KClass<*>): Boolean {
        return klass.hasAnnotation<A>()
    }
}
