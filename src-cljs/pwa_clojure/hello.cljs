(ns pwa-clojure.hello
  (:require [bidi.bidi :as bidi]
            [pwa-clojure.app-state :as app-state]
            [pwa-clojure.components :as components]
            [pwa-clojure.routes :as routes]
            [rum.core :as rum]))

(defn- get-current-path []
  (-> js/window .-location .-pathname))

(defn- get-container []
  (-> js/window .-document (.getElementById "container")))

(rum/defc reactive-component < rum/reactive []
  (components/pwa-component (rum/react app-state/app-state)))

(defn ^:export start-cljs-app [data]
  (let [current-path (get-current-path)
        {:keys [handler]} (bidi/match-route routes/pwa-routes current-path)]
    (reset! app-state/app-state {:handler handler :data data :url current-path :title "Hello, Cljs!"})
    (rum/mount (reactive-component) (get-container))))

(defn- test-clj-app []
  (start-cljs-app {:title "Hello, World!"}))

(test-clj-app)
