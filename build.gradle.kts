plugins {
    id("java")
}

group = "ru.gamesphere"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

allprojects {
    apply(plugin = "java")

    dependencies {
        implementation("org.jetbrains:annotations:23.0.0")
    }
}
