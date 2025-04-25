import org.gradle.api.tasks.JavaExec

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

plugins {
    java
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.41.2.1")
    implementation(project(":gemini"))
    implementation("dev.langchain4j:langchain4j:1.0.0-alpha1")
    implementation("dev.langchain4j:langchain4j-vertex-ai:1.0.0-alpha1")
    implementation("dev.langchain4j:langchain4j-vertex-ai-gemini:1.0.0-alpha1")
    implementation("org.slf4j:slf4j-simple:2.0.9")
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.11.3")
        }
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("Main")
}
