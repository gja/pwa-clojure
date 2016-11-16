(ns pwa-clojure.components
  (:require [rum.core :as rum]
            #?@(:cljs [[pwa-clojure.actions :as actions]])))

(def navigate #?(:cljs actions/navigate :clj (constantly nil)))
(def download-character #?(:cljs actions/download-character :clj (constantly nil)))

(rum/defc pwa-link [{:keys [href] :as params} & children]
  (into [:a (assoc params :on-click #(navigate % href))]
        children))

(rum/defc home-component [{:keys [characters]}]
  [:div
   [:h1 "Game of Thrones Characters"]
   [:p "Here are this weeks's top characters"]
   [:ul {}
    (map (fn [{:keys [id name]}]
           [:li {}
            (pwa-link {:href (str "/character/" id) :key id} name)
            [:a {:href "javascript:void(0)" :on-click #(download-character id)} "Download"]])
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
