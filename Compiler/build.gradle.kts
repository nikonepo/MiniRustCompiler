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

    // https://mvnrepository.com/artifact/org.bytedeco/llvm-platform
    implementation("org.bytedeco:llvm-platform:19.1.3-1.5.11")
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
