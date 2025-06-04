FROM ubuntu:latest
WORKDIR /home
RUN apt-get update && apt-get install -y bash
COPY run.sh /run.sh
RUN chmod +x /run.sh
CMD ["/run.sh"]