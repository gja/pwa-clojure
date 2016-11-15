(ns pwa-clojure.main
  (:require [pwa-clojure.app-state :as app-state]
            [pwa-clojure.pages :as pages]
            [pwa-clojure.navigation :as navigation]
            [rum.core :as rum]))

(defn- get-current-path []
  js/window.location.pathname)

(defn- get-container []
  (.getElementById js/window.document "container"))

(rum/defc reactive-component < rum/reactive []
  (pages/pwa-component (:handler (rum/react app-state/app-state))
                       (:data (rum/react app-state/app-state))))

(defn ^:export start-cljs-app []
  (navigation/move-to-page (get-current-path)
                           #(rum/mount (reactive-component) (get-container))))

(start-cljs-app)
