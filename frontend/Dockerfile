FROM node:18.17.1

WORKDIR /app

ENV PATH /app/node_modules/.bin:$PATH

COPY package.json /app/package.json
RUN npm install 
RUN npm install react-scripts@3.0.1 -g

CMD ["npm", "start"]