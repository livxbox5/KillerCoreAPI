plugins {
    id("java-library")
    id("maven-publish")
}

group = "org.killeryt"
version = "0.0.1-alpha-1"
val author = "KillerYT"

repositories {
    mavenCentral()
    mavenLocal()
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
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Vault API (используем JitPack)
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")

    // placeholderAPI
    compileOnly("me.clip:placeholderapi:2.11.6")

    // база данных
    compileOnly("org.postgresql:postgresql:42.7.3")
    compileOnly("com.h2database:h2:2.2.224")
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