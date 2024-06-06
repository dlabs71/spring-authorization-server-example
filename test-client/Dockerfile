# build stage
FROM node:16-alpine as build-stage

WORKDIR /app

COPY package.json .
COPY public ./public
COPY src ./src
COPY .env.docker .
COPY babel.config.js .
COPY vue.config.js .
CMD ["ls", "-la"]

RUN npm install
RUN npm run build-docker

# deploy stage
FROM nginx:stable-alpine as deploy-stage

RUN mkdir -p /var/log/nginx
RUN mkdir -p /var/www/html
RUN chown nginx:nginx /var/www/html

COPY --from=build-stage /app/dist /var/www/html/test-client
COPY ./nginx/nginx.conf /etc/nginx/nginx.conf
COPY nginx/test-client.conf /etc/nginx/conf.d/test-client.conf

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]