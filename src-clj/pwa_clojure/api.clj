(ns pwa-clojure.api
  (:require [cheshire.core :as json]
            [clojure.string :as str]
            [pwa-clojure.server.data :as data]))

;; This namespace is super simple
(defn- api-response [body]
  {:satus 200
   :headers {"Content-Type" "application/json"}
   :body (json/encode body)})

(defn- load-data-handler [handler]
  (fn [{:keys [route-params] :as request}]
    (-> handler
        (data/load-data route-params)
        api-response)))

(defn handler [handler]
  (load-data-handler handler))
