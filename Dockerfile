FROM nginx
LABEL maintainer="Violofan_(Balabanova)"
RUN apt-get update && apt-get install -y procps
WORKDIR /usr/share/nginx/html
ADD web/index.html /usr/share/nginx/html
CMD cd /usr/share/nginx/html && sed -e s/Docker/"$AUTHOR"/ index.html > index.html ; nginx -g 'daemon off;'
