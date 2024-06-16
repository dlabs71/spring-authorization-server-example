FROM nginx:stable-alpine

RUN mkdir -p /var/log/nginx
RUN mkdir -p /var/www/html
RUN chown nginx:nginx /var/www/html

COPY ./index.html /var/www/html/csrf-example/index.html
COPY ./nginx/nginx.conf /etc/nginx/nginx.conf
COPY nginx/csrf-example.conf /etc/nginx/conf.d/csrf-example.conf

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]