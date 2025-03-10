subprojects {
    apply(plugin = "java")

    group = "mipt.compiler.minirust"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
