(ns pwa-clojure.components
  (:require [rum.core :as rum]
            #?@(:cljs [[pwa-clojure.actions :as actions]])))

(def navigate #?(:cljs actions/navigate :clj (constantly nil)))
(def download-character #?(:cljs actions/download-character :clj (constantly nil)))

(rum/defc pwa-link [{:keys [href] :as params} & children]
  (into [:a (assoc params :on-click #(navigate % href))]
        children))

(rum/defc main-navigation []
  [:nav#main-navigation
   [:ul.main-navigation
    [:li [:a {:href "#hide-navigation" :class "toggle on"} [:span]]]
    [:li (pwa-link {:href "/"} "Home")]
    [:li (pwa-link {:href "/character/583"} "Jon Snow")]
    [:li (pwa-link {:href "/character/1303"} "Daenerys Targaryen")]
    [:li (pwa-link {:href "/character/529"} "Jamie Lannister")]]])

(rum/defc home-component [{:keys [characters]}]
  [:section.characters-grid
   [:ul {}
    (map (fn [{:keys [id name image]}]
           [:li.character {}
            [:figure.character-image-container
             [:img {:src image}]]
            [:div.character-details
             [:h3.character-name (pwa-link {:href (str "/character/" id)} name)]
             [:button.character-download {:on-click #(download-character id)} "Download"]]])
         characters)]])

(rum/defc character-component [{:keys [character]}]
  [:div.character-page
   [:div.character
    [:figure.character-image-container
     [:img {:src (:image character)}]]
    [:div.character-details
     [:h3.character-name (:name character)]]]

   [:div.character-info
    [:h3 "Aliases"]
    [:ul {}
     (map #(vector :li {} %) (:aliases character))]

    [:h3 "Seasons"]
    [:ul {}
     (map #(vector :li {} %) (:tvSeries character))]]])
