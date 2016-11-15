(ns pwa-clojure.actions)

(defn- navigate [e url]
  (.preventDefault e)
  (pwa-clojure.navigation/move-to-page url))

(defn- download-character [id]
  (js/console.log "Downloading Character" id)
  (pwa-clojure.client.data/prefetch :character :character-id id))
