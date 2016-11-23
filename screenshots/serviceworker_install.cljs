(def app-cache-name "pwa-clojure-app")

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

(.addEventListener js/self "install" #(.waitUntil % (install-service-worker %)))
