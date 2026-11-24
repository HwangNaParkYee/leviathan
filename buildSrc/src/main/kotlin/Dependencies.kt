object Dependencies {

    object SpringBoot {
        const val DATA_JPA = "org.springframework.boot:spring-boot-starter-data-jpa"
        const val SECURITY = "org.springframework.boot:spring-boot-starter-security"
        const val WEB = "org.springframework.boot:spring-boot-starter-web"
        const val TEST = "org.springframework.boot:spring-boot-starter-test"
    }

    object Jwt {
        const val API = "io.jsonwebtoken:jjwt-api:0.12.6"
        const val IMPL = "io.jsonwebtoken:jjwt-impl:0.12.6"
        const val JACKSON = "io.jsonwebtoken:jjwt-jackson:0.12.6"
    }

    object Jackson {
        const val DATABIND = "com.fasterxml.jackson.core:jackson-databind"
    }

    object Lombok {
        const val LOMBOK = "org.projectlombok:lombok"
    }

    object Database {
        const val H2 = "com.h2database:h2"
        const val MYSQL = "com.mysql:mysql-connector-j"
    }

    object Test {
        const val JUNIT_LAUNCHER = "org.junit.platform:junit-platform-launcher"
    }
}
