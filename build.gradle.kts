plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.alisacorporation"
version = "1.1.0"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2021.1")
    type.set("IC") // Target IDE Platform
    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    // Disable building searchable options since we don't have any
    withType<org.jetbrains.intellij.tasks.BuildSearchableOptionsTask> {
        enabled = false
    }
    
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
        options.encoding = "UTF-8"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    patchPluginXml {
        version.set(project.version.toString())
        sinceBuild.set("211") // Minimum IDE version
        //untilBuild.set("252.*") // Maximum IDE version
        changeNotes.set("""
            <ul>
                <li>Initial release: Copy file contents to clipboard from project view context menu</li>
            </ul>
        """.trimIndent())
        pluginDescription.set("""
            Copy file content to clipboard directly from project view context menu. Maybe useful, to share with Claude AI, ChatGPT etc..
        """.trimIndent())
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
