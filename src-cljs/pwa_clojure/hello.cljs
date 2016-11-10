(ns pwa-clojure.hello
  (:require
            [pwa-clojure.components :as components]
            [pwa-clojure.routes :as routes]
            [rum.core :as rum]))

(defn- get-current-path []
  (-> js/window .-location .-pathname))

(defn- get-container []
  (-> js/window .-document (.getElementById "container")))

(.debug js/console (get-container))

(defn start-cljs-app []
  (rum/mount (components/index-component "Hello, World!") (get-container)))

(start-cljs-app)
