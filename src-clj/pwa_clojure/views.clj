(ns pwa-clojure.views
  (:require [hiccup.page :refer [html5 include-js]]
            [pwa-clojure.pages :as pages]
            [rum.core :as rum]
            [pwa-clojure.server.data :as data]))

(defn layout [component title seo-fields]
  {:body
   (html5
    [:head
     [:title title]
     (seq seo-fields)]
    [:body
     [:div#container (rum/render-html component)]
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
  [:div.app-loading
   [:p "Please be patient"]])

(defn shell-page [request]
  (layout (empty-component) "PWA App!" []))
