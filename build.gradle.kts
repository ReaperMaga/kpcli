plugins {
    kotlin("jvm") version "2.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.2.0"
    id("org.graalvm.buildtools.native") version "0.10.6"
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.repsy.io/mvn/reapermaga/library") }
}

dependencies {
    // JLine3
    implementation("org.jline:jline-terminal:3.29.0")
    implementation("org.jline:jline-terminal-jni:3.29.0")
    implementation("org.jline:jline-reader:3.29.0")
    implementation("org.jline:jline-console-ui:3.29.0")

    implementation("com.github.reapermaga.library:common:0.1.13")
    implementation("net.lingala.zip4j:zip4j:2.11.5")
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use Kotlin Test test framework
            useKotlinTest("2.1.0")
        }
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
