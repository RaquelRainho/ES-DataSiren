FROM maven:3-jdk-8

RUN mkdir -p /root/.m2
COPY .m2/settings.xml /root/.m2/settings.xml