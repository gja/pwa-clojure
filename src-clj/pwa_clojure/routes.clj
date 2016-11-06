(ns pwa-clojure.routes
  (:use pwa-clojure.views
        [hiccup.middleware :only (wrap-base-url)])
  (:require [bidi.bidi :as bidi]
            [bidi.ring :as bring]
            [ring.middleware.resource :as resources]))

(def routes
  ["/" index-page])

(def app
  (-> (bring/make-handler routes)
      wrap-base-url
      (resources/wrap-resource "public")))
