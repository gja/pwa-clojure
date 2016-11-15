(ns pwa-clojure.components
  (:require [rum.core :as rum]))

(defn- navigate [e url]
  #?@(:cljs
      [(.preventDefault e)
       (pwa-clojure.navigation/move-to-page url)])
  nil)

(rum/defc pwa-link [{:keys [href] :as params} children]
  (into [:a (assoc params :on-click #(navigate % href))]
        children))

(rum/defc home-component [{:keys [characters]}]
  [:div
   [:h1 "Game of Thrones Characters"]
   [:p "Here are this weeks's top characters"]
   [:ul {}
    (map (fn [{:keys [id name]}]
           [:li {} (pwa-link {:href (str "/character/" id) :key id} name)])
         characters)]])

(rum/defc character-component [{:keys [character]}]
  [:div
   [:h1 "Awesome Characters - " (:name character)]

   [:p "Aliases"]
   [:ul {}
    (map #(vector :li {} %) (:aliases character))]

   [:p "Seasons"]
   [:ul {}
    (map #(vector :li {} %) (:tvSeries character))]])
