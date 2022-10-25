# MariaDB
FROM yobasystems/alpine-mariadb:10
# FROM mariadb:10.5

# Configuramos BBDD
ENV MYSQL_ROOT_PASSWORD 123
ENV MYSQL_USER blog
ENV MYSQL_PASSWORD blog1234
ENV MYSQL_DATABASE blog

# Copiamos los ficheros sql para que se ejecuten
COPY ./sql /docker-entrypoint-initdb.d/