plugins {
    kotlin("jvm") version "1.7.21"
    application
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "net.betterthanadventure"
version = "1.0-SNAPSHOT"

val nameOfMainClass = "com.mojang.escape.EscapeKt"
val lwjglVersion = "3.3.1"


repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-stb")
    implementation("org.lwjgl", "lwjgl-tinyfd")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-tinyfd", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = "natives-linux")
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = "natives-linux")
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = "natives-linux")
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = "natives-linux")
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = "natives-linux")
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = "natives-linux")
    runtimeOnly("org.lwjgl", "lwjgl-tinyfd", classifier = "natives-linux")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = "natives-macos")
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = "natives-macos")
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = "natives-macos")
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = "natives-macos")
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = "natives-macos")
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = "natives-macos")
    runtimeOnly("org.lwjgl", "lwjgl-tinyfd", classifier = "natives-macos")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = "natives-macos-arm64")
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = "natives-macos-arm64")
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = "natives-macos-arm64")
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = "natives-macos-arm64")
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = "natives-macos-arm64")
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = "natives-macos-arm64")
    runtimeOnly("org.lwjgl", "lwjgl-tinyfd", classifier = "natives-macos-arm64")
}

tasks.register<Copy>("grabJars") {
    from(layout.projectDirectory.file("../mods/prelude/build/libs/prelude-$version.jar"))
    into(layout.projectDirectory.dir("mods/"))
//    from(layout.projectDirectory.file("../mods/wolf3d/build/libs/wolf3d-$version.jar"))
//    into(layout.projectDirectory.dir("mods/"))
}
tasks["grabJars"].dependsOn += ":mods:prelude:build"
tasks["grabJars"].dependsOn += ":mods:prelude:jar"
//tasks["grabJars"].dependsOn += ":mods:wolf3d:build"
//tasks["grabJars"].dependsOn += ":mods:wolf3d:jar"
tasks["run"].dependsOn += "grabJars"

application {
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
        applicationDefaultJvmArgs = listOf("-XstartOnFirstThread")
    }
    mainClass.set(nameOfMainClass)
}

project.setProperty("mainClassName", nameOfMainClass)
tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    manifest {
        attributes["Main-Class"] = nameOfMainClass
    }
}