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

(rum/defc home-component [data]
  [:div
   [:h1 "Game of Thrones Characters"]
   [:p "Here are this weeks's top characters"]
   [:ul {}
    (map (fn [name]
           (pwa-link {:href (str "/character/" name) :key name} name))
         ["1" "2" "3"])]])

(rum/defc character-component [{:keys [name]}]
  [:div
   [:h1 "Awesome Characters - " name]
   [:p "Here is what people are saying"]])
