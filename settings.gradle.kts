val projectname: String = providers.gradleProperty("projectname").get()
rootProject.name = projectname

include("bahnApi")
include("common")
include("deploy")
include("model")
include("server")
include("support")

for (subproject in rootProject.children) {
    subproject.projectDir = file("module-" + subproject.name)
    subproject.buildFileName = "${subproject.name}.gradle.kts"
}