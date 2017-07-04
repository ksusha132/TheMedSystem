FROM frolvlad/alpine-oraclejdk8:slim


#install Spring Boot artifact
VOLUME /tmp
ADD turboenglish-0.0.1.jar  turboenglish.jar
RUN sh -c 'touch /turboenglish.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "java","-Djava.security.egd=file:/dev/./urandom", "-jar","/turboenglish.jar" ]