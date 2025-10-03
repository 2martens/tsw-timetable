import com.google.cloud.tools.jib.api.buildplan.ImageFormat

plugins {
    id("twomartens.jib")
}

dependencies {
    implementation(project(":server"))
}

jib {
    from {
        image = "public.ecr.aws/docker/library/amazoncorretto:21-alpine@sha256:fda60fd7965970ce7ed7ce789b18418647b56ac6112fc17df006337bdc6355c4"
        platforms {
            platform {
                architecture = "amd64"
                os = "linux"
            }
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = "ghcr.io/2martens/tsw-timetable"
        tags = setOf(
                "latest",
                properties["version"].toString().replace("+", "-"))
        auth {
            username = System.getenv("USERNAME")
            password = System.getenv("PASSWORD")
        }
    }
    container {
        mainClass = "de.twomartens.timetable.MainApplicationKt"
        jvmFlags = listOf("-XX:+UseContainerSupport",
                "-XX:MaxRAMPercentage=75.0")
        user = "nobody"
        format = ImageFormat.OCI
        labels = mapOf("org.opencontainers.image.description" to "Container image of the TSW Timetable backend")
    }
    outputPaths.digest = "${layout.buildDirectory.get().asFile}/jib-image.digest"
}
