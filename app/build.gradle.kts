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
    implementation("io.javalin:javalin:6.6.0")
    implementation("org.xerial:sqlite-jdbc:3.41.2.1")
    implementation(project(":gemini"))
    implementation("dev.langchain4j:langchain4j:1.0.0-alpha1")
    implementation("dev.langchain4j:langchain4j-vertex-ai:1.0.0-alpha1")
    implementation("dev.langchain4j:langchain4j-vertex-ai-gemini:1.0.0-alpha1")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
}

testing {
    suites {
       val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
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

tasks.withType<JavaExec> {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

configurations {
    runtimeClasspath
}

tasks.register("resolveDependencies") {
    doLast {
        configurations.runtimeClasspath.get().forEach { println(it) }
    }
}
