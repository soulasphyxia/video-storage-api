FROM maven:latest as stage1
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=stage1 /app/target/*.jar /app/*.jar
RUN apk update
RUN apk upgrade
RUN apk add --no-cache ffmpeg
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]