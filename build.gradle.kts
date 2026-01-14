import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.10.5"
}

group = "com.alisacorporation"
version = "1.1.3"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    // Use 2024.1 (build 241) as the base version for better compatibility
    intellijPlatform {
        intellijIdeaCommunity("2024.1")
    }
}

// Configure Gradle IntelliJ Platform Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
intellijPlatform {
    buildSearchableOptions = false

    pluginConfiguration {
        version = project.version.toString()

        ideaVersion {
            // Support from 2024.1 onwards - no upper limit
            // Build 241 = IntelliJ IDEA 2024.1
            // By not specifying untilBuild, the plugin will work with all future IDE versions
            sinceBuild = "241"
            // untilBuild is intentionally omitted for forward compatibility
        }

        changeNotes = """
            <ul>
                <li>v1.1.3: Improved compatibility with IntelliJ IDEA 2024.1 - 2025.3</li>
                <li>v1.1.2: Fixed compatibility with IntelliJ IDEA 2024.3+</li>
                <li>v1.1.2: Added proper ActionUpdateThread handling</li>
            </ul>
        """.trimIndent()

        description = """
            Copy file content to clipboard directly from project view context menu. 
            Useful for sharing code context with Claude AI, ChatGPT, and other AI assistants.
            
            Features:
            - Copy single file contents
            - Copy multiple files at once
            - Recursively copy directory contents
            - Simple right-click context menu integration
        """.trimIndent()
    }

    signing {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishing {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

tasks {
    // Use Java 17 for better compatibility with older IDEs
    // Java 17 is supported by IDEA 2024.1+
    withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
        options.encoding = "UTF-8"
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }

    patchPluginXml {
        // This ensures plugin.xml values are overridden by Gradle configuration
        sinceBuild.set("241")
        // untilBuild is intentionally not set - plugin will work with all future versions
    }
}