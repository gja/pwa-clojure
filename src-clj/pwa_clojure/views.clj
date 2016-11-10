(ns pwa-clojure.views
  (:require [hiccup.page :refer [html5 include-js]]
            [pwa-clojure.components :as components]
            [pwa-clojure.title :as title]
            [rum.core :as rum]))

(defn layout [component title seo-fields]
  {:body
   (html5
    [:head
     [:title title]
     (seq seo-fields)]
    [:body
     [:div#container (rum/render-html component)]
     (include-js "/js/main.js")])})

(defn pwa-page [handler {:keys [route-params]}]
  (let [data {:title "Hello, World!"}
        component (components/pwa-component {:handler handler
                                             :data data})]
    (layout component (title/title-for handler data) [])))
