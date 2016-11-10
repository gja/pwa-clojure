(ns pwa-clojure.app
  (:use [hiccup.middleware :only (wrap-base-url)])
  (:require [ring.middleware.resource :as resources]
            [pwa-clojure.routes :as routes]
            [pwa-clojure.server.bidi-handler :as bhandler]
            [pwa-clojure.views :as views]))

(defn- pwa-route-handler [handler]
  (case handler
    :home-page views/home-page))

(defn- api-route-handler [api]
  (fn [_]
    {:status 200
     :body "{}"}))

(def app
  (-> (constantly {:status 404, :body "not found"})
      (bhandler/make-bidi-handler routes/pwa-routes #'pwa-route-handler)
      (bhandler/make-bidi-handler routes/api-routes #'api-route-handler)
      wrap-base-url
      (resources/wrap-resource "public")))
