group = "pro.fairgame"
version = "1.0.0"

plugins {
    java
}

repositories {
    mavenCentral()
    maven(url = "https://www.jitpack.io")
}

dependencies {

}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "pro.fairgame.pngExtract.UnPacker"
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
