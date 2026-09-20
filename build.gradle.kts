plugins {
    java
    application
}

group = "sk.tuke.kpi.oop"
version = "1.0"

val gamelibVersion = "2.8.0"

val isMac = System.getProperty("os.name").contains("mac", ignoreCase = true)
val isAppleSilicon = isMac && System.getProperty("os.arch") == "aarch64"
val backend = if (isMac) "lwjgl2" else "lwjgl"

repositories {
    mavenCentral()
    maven(url = uri("https://repo.kpi.fei.tuke.sk/repository/maven-public"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("sk.tuke.kpi.gamelib.framework.Main")
}

dependencies {
    implementation("sk.tuke.kpi.gamelib:gamelib-framework:$gamelibVersion")
    implementation("sk.tuke.kpi.gamelib:gamelib-backend-$backend:$gamelibVersion")
    implementation("sk.tuke.kpi.gamelib:gamelib-inspector:$gamelibVersion")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

val x86JavaHome = "${System.getProperty("user.home")}/.gradle/x86-jdk21"

tasks.register("downloadX86Jdk") {
    onlyIf { isAppleSilicon && !file("$x86JavaHome/bin/java").exists() }
    doLast {
        val url = "https://cdn.azul.com/zulu/bin/zulu21.42.19-ca-jdk21.0.7-macosx_x64.tar.gz"
        val archive = file("${System.getProperty("java.io.tmpdir")}/zulu21-x64.tar.gz")
        println("Downloading x86_64 JDK for Apple Silicon Mac...")
        uri(url).toURL().openStream().use { input ->
            archive.outputStream().use { output -> input.copyTo(output) }
        }
        file(x86JavaHome).mkdirs()
        ProcessBuilder("tar", "-xzf", archive.absolutePath, "--strip-components=1", "-C", x86JavaHome)
            .inheritIO()
            .start()
            .waitFor()
        archive.delete()
        println("x86_64 JDK ready at $x86JavaHome")
    }
}

val lwjglNativesDir = "/tmp/lwjgl-natives"

tasks.register("extractLwjglNatives") {
    onlyIf { isMac && !file("$lwjglNativesDir/liblwjgl.dylib").exists() }
    doLast {
        file(lwjglNativesDir).mkdirs()
        val nativesJar = configurations.runtimeClasspath.get()
            .find { it.name.contains("lwjgl-platform") && it.name.contains("natives-osx") }
        if (nativesJar != null) {
            ProcessBuilder("jar", "xf", nativesJar.absolutePath)
                .directory(file(lwjglNativesDir))
                .inheritIO()
                .start()
                .waitFor()
        }
    }
}

tasks.named<JavaExec>("run") {
    if (isMac) {
        dependsOn("extractLwjglNatives")
        jvmArgs("-Djava.library.path=$lwjglNativesDir")
    }
    if (isAppleSilicon) {
        dependsOn("downloadX86Jdk")
        executable("$x86JavaHome/bin/java")
    }
}
