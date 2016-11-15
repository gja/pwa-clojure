(ns pwa-clojure.client.data
  (:require [pwa-clojure.routes :as routes]
            [bidi.bidi :as bidi]
            [ajax.core :as ajax]))

(defn- do-get [route callback]
  (ajax/GET route
            {:response-format :json
             :keywords? true
             :handler callback}))

(defn- cached-get [route callback]
  (if js/window.caches
    (-> js/window.caches
        (.match route)
        (.then (fn [response]
                 (if response
                   (-> response .json (js->clj {:keywordize-keys true}) (.then callback))
                   (do-get route callback)))))
    (do-get route callback)))

(defn load-data [handler args callback]
  (let [route (apply bidi/path-for routes/api-routes handler (flatten (vec args)))]
    (cached-get route callback)))
