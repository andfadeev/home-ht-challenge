FROM clojure:openjdk-11-lein-2.8.3
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY project.clj /usr/src/app/
RUN lein deps
COPY . /usr/src/app
EXPOSE 8080
CMD ["lein", "trampoline", "run", "config/production.edn"]