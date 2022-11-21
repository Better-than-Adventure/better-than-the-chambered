plugins {
    kotlin("jvm") version "1.7.21"
}

group = "net.betterthanadventure"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
}