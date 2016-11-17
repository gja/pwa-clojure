(ns pwa-clojure.main
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [pwa-clojure.app :as app]))

(defn -main []
  (jetty/run-jetty app/app {:port 3000}))
