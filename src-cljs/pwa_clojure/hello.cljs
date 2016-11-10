(ns pwa-clojure.hello
  (:require [bidi.bidi :as bidi]
            [pwa-clojure.components :as components]
            [pwa-clojure.routes :as routes]
            [rum.core :as rum]))

(defn- get-current-path []
  (-> js/window .-location .-pathname))

(defn- get-container []
  (-> js/window .-document (.getElementById "container")))

(defonce ^:private app-state (atom {}))

(rum/defc reactive-component < rum/reactive []
  (components/pwa-component (rum/react app-state)))

(defn ^:export start-cljs-app [data]
  (let [{:keys [handler]} (bidi/match-route routes/pwa-routes (get-current-path))]
    (reset! app-state {:handler handler :data data})
    (rum/mount (reactive-component) (get-container))))

(defn- test-clj-app []
  (start-cljs-app {:title "Hello, World!"}))

(test-clj-app)
