(ns pwa-clojure.service-worker
  (:require [cemerick.url :as url]
            [pwa-clojure.routes :as routes]
            [bidi.bidi :as bidi]
            [clojure.string :as str]))

(def app-cache-name "pwa-clojure-app")

(defn- purge-old-caches [e]
  (-> js/caches
      .keys
      (.then (fn [keys]
               (->> keys
                    (map #(when-not (contains? #{app-cache-name} %)
                            (.delete js/caches %)))
                    clj->js
                    js/Promise.all)))))

(def files-to-cache ["/js/main.js"
                     "/css/main.css"
                     "/css/pw_maze_white.png"
                     "/css/gears.svg"
                     "/shell.html"

                     "/api/characters"

                     "https://fonts.googleapis.com/css?family=Cardo:400,700,400italic|Open+Sans:400,800"])

(defn- install-service-worker [e]
  (js/console.log "[ServiceWorker] Installing")
  (-> js/caches
      (.open app-cache-name)
      (.then (fn [cache]
               (js/console.log "[ServiceWorker] Caching Shell")
               (.addAll cache (clj->js files-to-cache))))
      (.then (fn []
               (js/console.log "[ServiceWorker] Successfully Installed")))))

(defn- return-shell [e]
  (js/console.log "[ServiceWorker] Cached Page")
  (-> js/caches
      (.match "/shell.html")
      (.then (fn [response]
               (or response (js/fetch (.-request e)))))))

(defn- fetch-cached [request]
  (-> js/caches
      (.match request)
      (.then (fn [response]
               (or response (js/fetch request))))))

(defn- fetch-page-or-cached [path e]
  (cond
    (bidi/match-route routes/pwa-routes path)
    (return-shell e)

    :else
    (fetch-cached (.-request e))))

(defn- fetch-event [e]
  (js/console.log "[ServiceWorker] Fetch" (-> e .-request .-url))
  (let [request (.-request e)
        url (-> request .-url url/url)]
    (case (:host url)
      ("localhost" "pwa-clojure.staging.quintype.io")
      (fetch-page-or-cached (:path url) e)

      "my-images.net"
      (comment "Yes, you can cache images from other domains too!")

      (fetch-cached request))))

(.addEventListener js/self "install" #(.waitUntil % (install-service-worker %)))
(.addEventListener js/self "fetch" #(.respondWith % (fetch-event %)))
(.addEventListener js/self "activate" #(.waitUntil % (purge-old-caches %)))
