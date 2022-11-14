plugins {
    kotlin("multiplatform") version "1.7.21"
}

group = "com.mojang.escape"
version = "1.0-SNAPSHOT"

kotlin {
    jvm {
    }
    js(IR) {
        browser {

        }
    }
}

repositories {
    mavenCentral()
}
