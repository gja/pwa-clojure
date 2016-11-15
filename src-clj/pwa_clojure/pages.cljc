(ns pwa-clojure.pages
  (:require [pwa-clojure.components :as components]
            [rum.core :as rum]))

(def pages
  {:home-page {:data-fn (constantly [[:characters]])
               :component components/home-component
               :title-fn (constantly "Super Characters")}
   :character-page {:data-fn #(vec [:character :character-id (:character-id %)])
                    :component components/character-component
                    :title-fn (constantly "Super Characters")}})

(defn data-requirements [handler route-params]
  (let [data-fn (get-in pages [handler :data-fn])]
    (data-fn route-params)))

(rum/defc pwa-component [handler data]
  (let [component (get-in pages [handler :component])]
    (component data)))

(defn title [handler data]
  (let [title-fn (get-in pages [handler :title-fn])]
    (title-fn data)))
