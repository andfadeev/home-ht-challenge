FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/home-ht-challenge-0.0.1-SNAPSHOT-standalone.jar /home-ht-challenge/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/home-ht-challenge/app.jar"]
