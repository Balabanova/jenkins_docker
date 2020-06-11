FROM nginx
WORKDIR /usr/share/nginx/html
ADD web/Hello_docker.html /usr/share/nginx/html
