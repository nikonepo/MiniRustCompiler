plugins {
    id("java")
    id("application")
}

group = "mipt.compiler.minirust"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":IR"))
    implementation(project(":Parser"))
    implementation("org.antlr:antlr4:4.13.2")
}

application {
    mainClass.set("mipt.compiler.minirust.Main")
}

tasks.test {
    useJUnitPlatform()
}