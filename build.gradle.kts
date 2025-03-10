plugins {
    jacoco
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")

    group = "mipt.compiler.minirust"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
