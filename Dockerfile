FROM clojure
COPY . /usr/src/app
WORKDIR /usr/src/app

RUN lein deps
RUN lein cljsbuild once
CMD ["lein", "run"]
