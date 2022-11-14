repositories {
    mavenCentral()
}

plugins {
    kotlin("multiplatform")
}

group = "com.mojang.escape"
version = "1.0-SNAPSHOT"

kotlin {
    jvm {
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(project(":common"))
            }
        }
    }
}