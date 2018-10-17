FROM node:8

WORKDIR /user/src/app

COPY ./build /user/src/app

RUN ls -l

RUN npm install -g serve

EXPOSE 5000

CMD ["serve","-s"]