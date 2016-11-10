(ns pwa-clojure.main
  (:require [bidi.bidi :as bidi]
            [pwa-clojure.app-state :as app-state]
            [pwa-clojure.pages :as pages]
            [pwa-clojure.routes :as routes]
            [rum.core :as rum]))

(defn- get-current-path []
  (-> js/window .-location .-pathname))

(defn- get-container []
  (-> js/window .-document (.getElementById "container")))

(rum/defc reactive-component < rum/reactive []
  (pages/pwa-component (:handler (rum/react app-state/app-state))
                       (:data (rum/react app-state/app-state))))

(defn- fetch-data [handler route-params]
  {:name "Movie of the Year!"})

(defn ^:export move-to-page [path]
  (let [{:keys [handler route-params]} (bidi/match-route routes/pwa-routes path)
        data (fetch-data handler route-params)]
    (reset! app-state/app-state {:handler handler
                                 :data data
                                 :url path
                                 :title (pages/title handler data)})))

(defn ^:export start-cljs-app []
  (move-to-page (get-current-path))
  (rum/mount (reactive-component) (get-container)))

(start-cljs-app)
