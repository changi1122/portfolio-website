# 자바 소스를 빌드해 JAR 생성
FROM eclipse-temurin:17 AS int-build
LABEL description="changi1122 Homepage Builder"

RUN apt-get update && apt-get install -y git && rm -rf /var/lib/apt/lists/*

# 캐시 무효화용 인자 추가
ARG CACHE_BUST=1

RUN git clone https://github.com/changi1122/portfolio-website.git
WORKDIR /portfolio-website
RUN chmod 700 gradlew
RUN ./gradlew clean bootJar

# 빌드된 JAR를 경량화 이미지에 복사
FROM eclipse-temurin:17-jre-alpine
LABEL description="changi1122 Homepage Application"
EXPOSE 1123

COPY --from=int-build portfolio-website/build/libs/portfolio-website-0.0.1-SNAPSHOT.jar /opt/portfolio.jar
WORKDIR /opt

ENTRYPOINT ["java", "-jar", "portfolio.jar"]