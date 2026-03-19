plugins {
    kotlin("jvm") version "2.2.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}


application {
    mainClass.set("MainKt")
}


tasks.test {
    useJUnitPlatform()
}


tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}