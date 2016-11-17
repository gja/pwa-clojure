(ns pwa-clojure.main
  (:require [pwa-clojure.app-state :as app-state]
            [pwa-clojure.pages :as pages]
            [pwa-clojure.navigation :as navigation]
            [rum.core :as rum]
            [pwa-clojure.components :as components]))

(defn- get-current-path []
  js/window.location.pathname)

(defn- get-container [name]
  (.getElementById js/window.document name))

(rum/defc reactive-component < rum/reactive []
  (pages/pwa-component (:handler (rum/react app-state/app-state))
                       (:data (rum/react app-state/app-state))))

(defn- make-progressive! []
  (when js/navigator.serviceWorker
    (.register js/navigator.serviceWorker "/service-worker.js")))

(defonce app-loaded (atom false))
(defn load-app []
  (when-not @app-loaded
    (reset! app-loaded true)
    (rum/mount (reactive-component) (get-container "container"))
    (rum/mount (components/main-navigation) (get-container "header-container"))))

(defn ^:export start-cljs-app []
  (navigation/move-to-page (get-current-path) load-app)
  (make-progressive!))

(start-cljs-app)
