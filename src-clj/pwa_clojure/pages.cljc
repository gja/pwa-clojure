(ns pwa-clojure.pages
  (:require [pwa-clojure.components :as components]
            [rum.core :as rum]))

(def pages
  {:home-page {:data-fn (constantly [[:movies]])
               :component components/home-component
               :title-fn (constantly "Super Movies")}
   :movie-page {:data-fn #(vec [:movie :movie-id (:movie-id %)])
                :component components/movie-component
                :title-fn (constantly "Super Movie")}})

(defn data-requirements [handler route-params]
  (let [data-fn (get-in pages [handler :data-fn])]
    (data-fn route-params)))

(rum/defc pwa-component [handler data]
  (let [component (get-in pages [handler :component])]
    (component data)))

(defn title [handler data]
  (let [title-fn (get-in pages [handler :title-fn])]
    (title-fn data)))
