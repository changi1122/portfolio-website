FROM openjdk:17
LABEL description="changi1122 Homepage"
EXPOSE 1122

COPY ./build/libs/portfolio-website-0.0.1-SNAPSHOT.jar /opt/portfolio.jar
WORKDIR /opt

ENTRYPOINT ["java", "-jar", "portfolio.jar"]