plugins {
    `common-conventions`
    `exposed-conventions`
    `kord-conventions`
    application
}

dependencies {
    implementation(project(":hisoka-common"))
}

application {
    mainClass = "net.kingchev.Main"
}