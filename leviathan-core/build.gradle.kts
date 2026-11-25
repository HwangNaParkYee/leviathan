plugins {
    id("java-common")
}

dependencies {
    implementation(project(":leviathan-common"))
    implementation(Dependencies.SpringBoot.DATA_JPA)

    runtimeOnly(Dependencies.Database.MYSQL)
    runtimeOnly(Dependencies.Database.H2)
}
