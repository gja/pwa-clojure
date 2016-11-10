(ns pwa-clojure.components
  (:require [rum.core :as rum]))

(defn- navigate [e url]
  #?@(:cljs
      [(.preventDefault e)
       (pwa-clojure.main/move-to-page url)])
  nil)

(rum/defc pwa-link [{:keys [href] :as params} children]
  (into [:a (assoc params :on-click #(navigate % href))]
        children))

(rum/defc home-component [data]
  [:div
   [:h1 "Welcome To Super Movies"]
   [:p "Here are this weeks's top movies"]
   [:ul {}
    (map (fn [name]
           (pwa-link {:href (str "/movie/" name) :key name} name))
         ["Foo" "Bar" "Baz"])]])

(rum/defc movie-component [{:keys [name]}]
  [:div
   [:h1 "Awesome Movies - " name]
   [:p "Here is what people are saying"]])
