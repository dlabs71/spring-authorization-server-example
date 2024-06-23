FROM bellsoft/liberica-openjdk-alpine:17.0.7-7

ENV TZ=Europe/Moscow
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./target/*.jar app.jar

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000", "--add-opens=java.base/java.util=ALL-UNNAMED", "-jar", "app.jar"]