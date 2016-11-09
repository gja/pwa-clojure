(ns pwa-clojure.views
  (:require [hiccup.page :refer [html5 include-js]]
            [rum.core :as rum]))

(rum/defc index-component [title]
  [:h1 title])

(defn index-page [_]
  {:body
   (html5
    [:head
     [:title "Hello World"]
     (include-js "/js/main.js")]
    [:body
     [:div.container
      (rum/render-html (index-component "Hello, World!"))]])})
