// 실행 모듈 설정 (bootJar를 만드는 모듈에만 적용)
plugins {
    id("java-common")
    id("org.springframework.boot")
}

// jar 이름은 모듈 이름이 아니라 프로젝트 이름으로 만든다 (leviathan-0.0.1-SNAPSHOT.jar).
// 배포 스크립트가 서버에서 *-SNAPSHOT.jar 로 실행하므로 이전 jar와 이름이 같아야 덮어쓴다
tasks.bootJar {
    archiveBaseName.set(rootProject.name)
}

// 실행 가능한 jar는 bootJar 하나만 만든다
tasks.jar {
    enabled = false
}

// docker_problems_file 경로가 user.dir 기준이므로 프로젝트 루트에서 실행한다
tasks.bootRun {
    workingDir = rootProject.projectDir
}
