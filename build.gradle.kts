plugins {
    id("twomartens.versions")
    id("twomartens.nebula-release")
}

nebulaRelease {
    addReleaseBranchPattern("/main/")
}

versionCatalogUpdate {
    sortByKey.set(false)
    keep {
        keepUnusedVersions = true
    }
}