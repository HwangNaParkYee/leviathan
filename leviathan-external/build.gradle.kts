plugins {
    id("java-common")
}

dependencies {
    implementation(project(":leviathan-core"))
    implementation(Dependencies.SpringBoot.WEB)
    implementation(Dependencies.Jackson.DATABIND)
}
