val projectname: String = providers.gradleProperty("projectname").get()
rootProject.name = projectname

include("common")
include("server")
include("deploy")
include("support")

for (subproject in rootProject.children) {
    subproject.projectDir = file("module-" + subproject.name)
    subproject.buildFileName = "${subproject.name}.gradle.kts"
}