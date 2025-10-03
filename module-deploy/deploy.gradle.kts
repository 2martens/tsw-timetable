plugins {
    id("twomartens.jib")
}

dependencies {
    implementation(project(":server"))
}

jib {
    from {
        image = "public.ecr.aws/amazoncorretto:" + properties["projectSourceCompatibility"] + "-alpine"
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
    }
    outputPaths.digest = "${layout.buildDirectory.get().asFile}/jib-image.digest"
}
