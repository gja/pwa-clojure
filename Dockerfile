FROM clojure

RUN mkdir -p /app

WORKDIR /app

COPY project.clj /app
RUN lein deps

COPY . /app

RUN lein cljsbuild once
CMD ["lein", "run"]
