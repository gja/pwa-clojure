(ns pwa-clojure.service-worker
  (:require [cemerick.url :as url]
            [pwa-clojure.routes :as routes]
            [bidi.bidi :as bidi]
            [clojure.string :as str]))

(def app-cache-name "pwa-clojure-app")
(def data-cache-name "pwa-clojure-data")

(defn- purge-old-caches [e]
  (-> js/caches
      .keys
      (.then (fn [keys]
               (->> keys
                    (map #(when-not (contains? #{app-cache-name data-cache-name} %)
                            (.delete js/caches %)))
                    clj->js
                    js/Promise.all)))))

(def files-to-cache ["/js/main.js"
                     "/shell.html"])


(defn- install-service-worker [e]
  (js/console.log "[ServiceWorker] Installing")
  (-> js/caches
      (.open app-cache-name)
      (.then (fn [cache]
               (js/console.log "[ServiceWorker] Caching Shell")
               (.addAll cache (clj->js files-to-cache))))))

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
  (let [url (-> e .-request .-url url/url)]
    (case (:host url)
      ("localhost" "pwa-clojure.staging.quintype.io")
      (fetch-cached (:path url) e)

      "my-images.net"
      (comment "Yes, you can cache images from other domains too!")

      (js/fetch (.-request e)))))

(.addEventListener js/self "install" #(.waitUntil % (install-service-worker %)))
(.addEventListener js/self "fetch" #(.respondWith % (fetch-event %)))
(.addEventListener js/self "activate" #(.waitUntil % (purge-old-caches %)))
