plugins {
    id("java-library")
    id("maven-publish")
}

group = "org.killeryt"
version = "1.0.0"
val author = "KillerYT"

repositories {
    mavenCentral()
    mavenLocal() // на случай локальной установки Vault
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Vault API (используем JitPack)
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// Настройка подстановки переменных в ресурсы (plugin.yml)
tasks.processResources {
    val props = mapOf(
        "version" to project.version,
        "author" to author
    )
    inputs.properties(props)
    filesMatching("plugin.yml") {
        expand(props)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}