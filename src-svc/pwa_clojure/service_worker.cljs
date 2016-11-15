(ns pwa-clojure.service-worker)

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

(.addEventListener js/self "install" install-service-worker)
