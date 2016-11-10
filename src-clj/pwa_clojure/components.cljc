(ns pwa-clojure.components
  (:require [rum.core :as rum]))

(rum/defc index-component [{:keys [title]}]
  [:h1 title])

(rum/defc pwa-component [{:keys [handler data]}]
  (case handler
    :home-page (index-component data)))
