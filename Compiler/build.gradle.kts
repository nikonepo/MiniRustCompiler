plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "mipt.compiler.minirust"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":IR"))
    implementation(project(":Parser"))
    implementation(project(":Lexer"))
    implementation("org.antlr:antlr4:4.13.2")
}

application {
    mainClass.set("mipt.compiler.minirust.Main")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    shadowJar {
        archiveBaseName.set("MiniRustCompiler")
        archiveClassifier.set("")
        archiveVersion.set("")
        mergeServiceFiles()
        manifest {
            attributes(mapOf(
                "Main-Class" to "mipt.compiler.minirust.Main"
            ))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}
