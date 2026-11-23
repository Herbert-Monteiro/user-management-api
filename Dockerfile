# ---------- Etapa 1: compilar ----------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon > /dev/null

COPY src src
RUN ./gradlew bootJar -x test --no-daemon \
    && cp "$(ls build/libs/*.jar | grep -v plain | head -n 1)" app.jar

# ---------- Etapa 2: rodar ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

RUN useradd --system --no-create-home appuser
COPY --from=build /app/app.jar app.jar
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
