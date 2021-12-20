FROM adoptopenjdk/openjdk11:jre-11.0.11_9-debianslim

ARG VERSION
ENV VERSION=${VERSION}

RUN mkdir -p /workdir

COPY target/post-0.0.1-SNAPSHOT.jar /workdir/post-0.0.1-SNAPSHOT.jar
RUN chmod +x "/workdir/post-0.0.1-SNAPSHOT.jar"

WORKDIR /workdir
EXPOSE 8080
ENTRYPOINT java -jar /workdir/post-0.0.1-SNAPSHOT.jar $0 $@
