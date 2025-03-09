plugins {
    id("java")
}

group = "mipt.compiler.minirust.parser"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.antlr/antlr4
    implementation("org.antlr:antlr4:4.13.2")
}