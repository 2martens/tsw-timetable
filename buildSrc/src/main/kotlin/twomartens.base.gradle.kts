plugins {
    id("idea")
}

val projectgroup: String = providers.gradleProperty("projectgroup").get()
group = projectgroup