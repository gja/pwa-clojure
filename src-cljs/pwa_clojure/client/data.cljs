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

(defn- response->clj [response]
  (-> response .json (.then #(js->clj % :keywordize-keys true))))

(defn- cache-response [route response]
  (js/console.log "Caching Data" route)
  (let [clone (.clone response)]
    (-> js/caches
        (.open data-cache-name)
        (.then #(.put % route clone)))))

(defn- cached-get [route]
  (-> (.match js/window.caches route)
      (.then
       (fn [response]
         (if response
           (response->clj response)
           (-> (js/fetch route)
               (.then
                (fn [response]
                  (cache-response route response)
                  (response->clj response)))))))))

(defn load-data [handler args callback]
  (let [route (apply bidi/path-for routes/api-routes handler (flatten (vec args)))]
    (if (and js/window.caches js/fetch)
      (-> (cached-get route) (.then callback))
      (fallback-get route callback))))

(defn prefetch [handler & args]
  (let [route (apply bidi/path-for routes/api-routes handler args)]
    (when js/window.caches
      (-> js/window.caches
          (.open data-cache-name)
          (.then #(.add % route))))))
