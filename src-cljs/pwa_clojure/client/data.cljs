(ns pwa-clojure.client.data
  (:require [pwa-clojure.routes :as routes]
            [bidi.bidi :as bidi]
            [ajax.core :as ajax]))

(defn load-data [handler args callback]
  (let [route (apply bidi/path-for routes/api-routes handler (flatten (vec args)))]
    (ajax/GET route
              {:response-format :json
               :keywords? true
               :handler callback})))
