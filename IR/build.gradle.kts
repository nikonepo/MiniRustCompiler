plugins {
    id("java")
}

group = "mipt.compiler.minirust.ir"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.antlr:antlr4:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(project(":Parser"))
}

tasks.test {
    useJUnitPlatform()
}