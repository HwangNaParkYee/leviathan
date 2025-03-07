# Amazon Corretto 21 JDK 사용
FROM amazoncorretto:21

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일 복사 (Gradle 빌드 후 .jar 파일이 필요함)
COPY build/libs/IamWnatBeAwsmaster-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 실행 포트
EXPOSE 8080

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
