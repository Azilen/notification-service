FROM postgres:12.3
MAINTAINER hushen.savani@azilen.com
ENV POSTGRES_HOST=azilen-notification-postgresql
RUN mkdir -p scripts

COPY azilen/scripts scripts

COPY ./docker-entrypoint.sh /
RUN chmod 755 docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]
