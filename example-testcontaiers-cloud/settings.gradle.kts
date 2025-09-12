plugins {
    id("com.gradle.develocity") version "4.1.1"
    id("com.gradle.common-custom-user-data-gradle-plugin") version "2.3"
}

val isCI = System.getenv("CI") != null // adjust to your CI provider

develocity {
    server = "https://ge.solutions-team.gradle.com/" // adjust to your Develocity server

    buildScan {
        uploadInBackground = !isCI

        val maxLocalExecutors = providers.gradleProperty("maxLocalExecutors").map { it.toInt() }.orElse(0)
        val maxRemoteExecutors = providers.gradleProperty("maxRemoteExecutors").map { it.toInt() }.orElse(0)

        if (maxRemoteExecutors.get() > 0) tag("TD") else tag ("NO_TD")
        value("maxLocalExecutors", maxLocalExecutors.get().toString())
        value("maxRemoteExecutors", maxRemoteExecutors.get().toString())
    }
}

buildCache {
    local {
        isEnabled = false
    }

    remote(develocity.buildCache) {
        isEnabled = false
    }
}

rootProject.name = "cloud.testcontainers.example.example-testcontainers-cloud"
