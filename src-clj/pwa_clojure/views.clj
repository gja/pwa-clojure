(ns pwa-clojure.views
  (:require [hiccup.page :refer [html5 include-js]]
            [pwa-clojure.pages :as pages]
            [rum.core :as rum]
            [pwa-clojure.server.data :as data]
            [pwa-clojure.components :as components]))

(defn layout [component title seo-fields]
  {:headers {"Content-Type" "text/html"}
   :body
   (html5
    [:head
     [:title title]
     [:link {:rel "stylesheet" :href "/css/main.css"}]
     (seq seo-fields)
     [:link {:rel "stylesheet" :href "https://fonts.googleapis.com/css?family=Cardo:400,700,400italic|Open+Sans:400,800"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
    [:body
     [:div.container
      [:div#header-container
       (rum/render-html (components/main-navigation))]
      [:header
       [:a.toggle {:href "#main-navigation" :title "menu"} [:span]]
       [:h1.title "Game of Thrones"]]
      [:div#container (rum/render-html component)]]
     (include-js "/js/main.js")])})

(defn- seo-fields [handler data]
  (case handler
    :home-page [[:meta {:name "description" :content "Home Page"}]]
    []))

(defn pwa-page [handler {:keys [route-params]}]
  (let [[data-handler data-args] (first (pages/data-requirements handler route-params))
        data (data/load-data data-handler data-args)
        component (pages/pwa-component handler data)]
    (layout component (pages/title handler data) (seo-fields handler data))))

(rum/defc empty-component []
  [:div.app-loading])

(defn shell-page [request]
  (layout (empty-component) "PWA App!" []))
