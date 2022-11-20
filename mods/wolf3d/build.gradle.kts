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

tasks.register<Copy>("copyJar") {
    from(layout.buildDirectory.file("libs/prelude-$version.jar"))
    into(layout.projectDirectory.dir("../../core/mods"))
}

tasks["jar"].dependsOn += tasks["copyJar"]