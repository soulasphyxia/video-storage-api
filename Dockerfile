FROM maven:latest as stage1
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=stage1 /app/target/*.jar /app/*.jar
COPY --from=stage1 /app/ffmpeg_script.sh /app/ffmpeg_script.sh
RUN apk update
RUN apk upgrade
RUN apk add --no-cache ffmpeg
RUN apk add --no-cache bash
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]