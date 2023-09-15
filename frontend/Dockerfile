FROM node:18.16.1
ENV TZ=Asia/Seoul
WORKDIR /app
COPY package*.json ./
RUN npm install
RUN npm install -g serve
COPY . .
RUN NODE_ENV=production npm run build
ENTRYPOINT ["serve", "-s", "dist"]
