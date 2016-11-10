(ns pwa-clojure.components
  (:require [rum.core :as rum]))

(rum/defc index-component [title]
  [:h1 title])
