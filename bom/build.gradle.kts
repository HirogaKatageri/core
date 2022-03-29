plugins {
    `version-catalog`
    `maven-publish`
}

catalog {
    versionCatalog {
        library("coil", Deps.Image.COIL)
        library("image-picker", Deps.Image.PICKER)

        library("leak-canary", Deps.DebugTools.LEAK_CANARY)
        library("chucker", Deps.DebugTools.CHUCKER)
        library("chucker-noop", Deps.DebugTools.CHUCKER_NOOP)
        library("timber", Deps.DebugTools.TIMBER)

        library("app-auth", Deps.Security.APP_AUTH)

        library("koin-core", Deps.Koin.CORE)
        library("koin-android", Deps.Koin.ANDROID)
        library("koin-workmanager", Deps.Koin.WORKMANAGER)
        library("koin-navigation", Deps.Koin.NAVIGATION)
        library("koin-compose", Deps.Koin.COMPOSE)
        library("koin-test", Deps.Test.KOIN_CORE)
        library("koin-junit4", Deps.Test.KOIN_JUNIT4)

        library("retrofit", Deps.Network.RETROFIT)
        library("retrofit-moshi-converter", Deps.Network.RETROFIT_MOSHI)
        library("okhttp", Deps.Network.OKHTTP)
        library("okhttp-logging-interceptor", Deps.Network.OKHTTP_LOGGING)
        library("moshi", Deps.Network.MOSHI)
        library("moshi-codegen", Deps.Network.MOSHI_CODEGEN)
        library("network-response-adapter", Deps.Network.NETWORK_RESPONSE)

        library("androidx-test-core", Deps.Test.ANDROIDX_CORE)
        library("androidx-test-lifecycle", Deps.Test.LIFECYCLE)
        library("androidx-test-navigation", Deps.Test.NAVIGATION)
        library("androidx-test-runner", Deps.Test.ANDROIDX_RUNNER)
        library("androidx-test-rules", Deps.Test.ANDROIDX_RULES)
        library("androidx-test-fragment", Deps.Test.ANDROIDX_FRAGMENT)
        library("androidx-test-ext", Deps.Test.ANDROIDX_JUNIT)
        library("androidx-test-espresso-core", Deps.Test.ESPRESSO)
        library("robolectric", Deps.Test.ROBOLECTRIC)
        library("mockk", Deps.Test.MOCKK)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["versionCatalog"])

            groupId = "dev.hirogakatageri"
            artifactId = "bom"
            version = Versions.Bom.VERSION

            pom {
                name.set("Bill of Materials")
                description.set("Bill of Materials for libraries commonly used by Hiroga Katageri")

                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://github.com/HirogaKatageri/core/blob/master/LICENSE.md")
                    }
                }

                developers {
                    developer {
                        id.set("HirogaKatageri")
                        name.set("Gian Patrick Quintana")
                        email.set("gianpatrick27@gmail.com")
                    }
                }
            }
        }

        repositories {
            maven {
                name = "GithubPackages"
                url = uri("https://maven.pkg.github.com/hirogakatageri/core")
                credentials {
                    username = System.getenv("GITHUB_USER")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}
