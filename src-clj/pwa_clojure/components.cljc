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
    [:li (pwa-link {:href "/character/531"} "Jon Snow")]
    [:li (pwa-link {:href "/character/1303"} "Daenerys Targaryen")]]])

(rum/defc home-component [{:keys [characters]}]
  [:section
   [:ul {}
    (map (fn [{:keys [id name]}]
           [:li.character {}
            [:figure.character-image-container
             [:img {:src "http://vignette2.wikia.nocookie.net/gameofthrones/images/4/4c/JonSnowTightened-S4.jpg/revision/latest?cb=20140322183538"}]]
            [:div.character-details
             [:h3.character-name name]
             [:button.character-download {:on-click #(download-character id)} "Download"]]])
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
