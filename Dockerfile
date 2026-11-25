# Amazon Corretto JDK 21 이미지 사용
FROM amazoncorretto:21

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 빌드 후 생성된 .jar 파일을 복사
COPY leviathan-api/build/libs/leviathan-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 실행 포트
EXPOSE 8080

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
