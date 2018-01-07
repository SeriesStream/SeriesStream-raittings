FROM openjdk:8-jre-alpine

MAINTAINER ml9987@student.uni-lj.si

RUN mkdir /app

WORKDIR /app

ADD ./target/series-stream-rattings-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "series-stream-rattings-0.0.1-SNAPSHOT.jar"]