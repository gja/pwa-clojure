(ns pwa-clojure.client.data
  (:require [pwa-clojure.routes :as routes]
            [bidi.bidi :as bidi]
            [ajax.core :as ajax]))

(def data-cache-name "pwa-clojure-data")

(defn- fallback-get [route callback]
  (ajax/GET route
            {:response-format :json
             :keywords? true
             :handler callback}))

(defn- fetch-callback [response callback]
  (-> response
      .json
      (.then #(js->clj % :keywordize-keys true))
      (.then callback)))

(defn- cache-response [route response]
  (js/console.log "Caching Data" route)
  (let [clone (.clone response)]
    (-> js/caches
        (.open data-cache-name)
        (.then #(.put % route clone)))))

(defn- cached-get [route callback]
  (-> (.match js/window.caches route)
      (.then
       (fn [response]
         (if response
           (fetch-callback response callback)
           (-> (js/fetch route)
               (.then
                (fn [response]
                  (cache-response route response)
                  (fetch-callback response callback)))))))))

(defn load-data [handler args callback]
  (let [route (apply bidi/path-for routes/api-routes handler (flatten (vec args)))]
    (if (and js/window.caches js/fetch)
      (cached-get route callback)
      (fallback-get route callback))))
