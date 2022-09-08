FROM adoptopenjdk/openjdk16:ubi
COPY build/libs/pagamentos-0.0.1-SNAPSHOT.jar pagamentos-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/pagamentos-0.0.1-SNAPSHOT.jar"]