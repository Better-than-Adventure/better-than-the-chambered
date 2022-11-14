plugins {
    kotlin("multiplatform")
}

group = "com.mojang.escape"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        browser {
        }
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":common"))
            }
        }
    }
}