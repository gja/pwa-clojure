(ns pwa-clojure.hello
  (:require [bidi.bidi :as bidi]
            [pwa-clojure.components :as components]
            [pwa-clojure.routes :as routes]
            [rum.core :as rum]))

(defn- get-current-path []
  (-> js/window .-location .-pathname))

(defn- get-container []
  (-> js/window .-document (.getElementById "container")))

(defn start-cljs-app []
  (let [{:keys [handler]} (bidi/match-route routes/pwa-routes (get-current-path))]
    (rum/mount (components/index-component (str handler "Hello, World!")) (get-container))))

(start-cljs-app)
