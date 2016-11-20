(ns pwa-clojure.pages
  (:require [pwa-clojure.components :as components]
            [rum.core :as rum]))

(def pages
  {:home-page {:data-fn (constantly [[:characters {}]])
               :component #'components/home-component
               :title-fn (constantly "PWA Of Thrones")}
   :character-page {:data-fn #(vector [:character {:character-id (:character-id %)}])
                    :component #'components/character-component
                    :title-fn #(str "Thrones PWA - " (get-in % [:character :name]))}})

(defn data-requirements [handler params]
  (let [data-fn (get-in pages [handler :data-fn])]
    (data-fn params)))

(defn title [handler data]
  (let [title-fn (get-in pages [handler :title-fn])]
    (title-fn data)))

(rum/defc pwa-component [handler data]
  (let [component (get-in pages [handler :component])]
    (component data)))
