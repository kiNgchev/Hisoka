plugins {
    id("java")
    me.champeau.jmh
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("jmh-core").get())
    implementation(libs.findLibrary("jmh.generator.annprocess").get())
}