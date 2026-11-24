plugins {
    id("java-common")
    id("org.springframework.boot")
}

tasks.bootJar {
    archiveBaseName.set(rootProject.name)
}

tasks.jar {
    enabled = false
}

tasks.bootRun {
    workingDir = rootProject.projectDir
}
