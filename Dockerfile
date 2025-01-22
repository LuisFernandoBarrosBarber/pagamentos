FROM adoptopenjdk/openjdk16:ubi

# Instalar ping, nslookup e hostname
RUN yum install -y iputils bind-utils util-linux && \
    yum clean all

COPY build/libs/pagamentos-0.0.1-SNAPSHOT.jar pagamentos-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/pagamentos-0.0.1-SNAPSHOT.jar"]