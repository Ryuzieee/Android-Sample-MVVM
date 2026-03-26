pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Android-Sample-MVVM"
include(":app")
include(":core")
include(":core:domain")
include(":core:data")
include(":core:ui")
include(":core:testing")
include(":feature")
include(":feature:list")
include(":feature:detail")
include(":feature:search")
include(":feature:favorites")
