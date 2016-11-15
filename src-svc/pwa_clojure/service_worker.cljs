(ns pwa-clojure.service-worker
  (:require [cemerick.url :as url]
            [pwa-clojure.routes :as routes]
            [bidi.bidi :as bidi]
            [clojure.string :as str]))

(def app-cache-name "pwa-clojure-app")
(def data-cache-name "pwa-clojure-data")

(def files-to-cache ["/js/main.js"
                     "/shell.html"])

(defn- install-service-worker [e]
  (js/console.log "[ServiceWorker] Installing")
  (js/e.waitUntil
   (-> js/caches
       (.open app-cache-name)
       (.then (fn [cache]
                (js/console.log "[ServiceWorker] Caching Shell")
                (.addAll cache (clj->js files-to-cache)))))))

(defn- return-shell [e]
  (js/console.log "[ServiceWorker] Cached Page")
  (-> js/caches
      (.match "/shell.html")
      (.then (fn [response]
               (or response (js/fetch (.-request e)))))))

(defn- fetch-cached [path e]
  (cond
    (bidi/match-route routes/pwa-routes path)
    (return-shell e)

    :else
    (js/fetch (.-request e))))

(defn- fetch-event [e]
  (js/console.log "[ServiceWorker] Fetch" (-> e .-request .-url))
  (.respondWith
   e
   (let [url (-> e .-request .-url url/url)]
     (case (:host url)
       ("localhost" "pwa-clojure.staging.quintype.io")
       (fetch-cached (:path url) e)

       "my-images.net"
       (comment "Yes, you can cache images from other domains too!")

       (js/fetch (.-request e))))))

(.addEventListener js/self "install" install-service-worker)
(.addEventListener js/self "fetch" fetch-event)
