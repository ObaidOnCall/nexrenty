FROM ubuntu:rolling

RUN mkdir -p /main_project

COPY ./target/carental /main_project/


WORKDIR /main_project

ENTRYPOINT ["./carental"]