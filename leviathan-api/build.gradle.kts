plugins {
    id("spring-app")
}

dependencies {
	implementation(project(":leviathan-common"))
    implementation(project(":leviathan-core"))
    implementation(project(":leviathan-external"))

	implementation(Dependencies.SpringBoot.DATA_JPA)
    implementation(Dependencies.SpringBoot.WEB)
    implementation(Dependencies.Jackson.DATABIND)

    // jwt
    runtimeOnly(Dependencies.Jwt.JACKSON)
    implementation(Dependencies.Jwt.API)
	runtimeOnly(Dependencies.Jwt.IMPL)

    //security
    implementation(Dependencies.SpringBoot.SECURITY)
}
