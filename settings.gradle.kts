pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name = "GithubPackages"
            url = uri("https://maven.pkg.github.com/hirogakatageri/core")
            credentials {
                username = System.getenv("GITHUB_USER")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        maven { url = uri("https://jitpack.io") }
    }
}

include(":core")
include(":bom")

if (!isJitpack() && !isCircleCi()) {
    include(":sample")
}

fun isJitpack(): Boolean = try {
    System.getenv("JITPACK").toBoolean()
} catch (ex: Exception) {
    false
}

fun isCircleCi(): Boolean = try {
    System.getenv("CIRCLECI").toBoolean()
} catch (ex: Exception) {
    false
}
