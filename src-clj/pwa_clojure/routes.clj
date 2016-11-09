(ns pwa-clojure.routes
  (:use [hiccup.middleware :only (wrap-base-url)])
  (:require [ring.middleware.resource :as resources]
            [pwa-clojure.server.bidi-handler :as bhandler]
            [pwa-clojure.views :as views]))

(defn- pwa-route-handler [handler]
  (case handler
    :index views/index-page))

(def pwa-routes
  ["/" :index])

(defn- api-route-handler [api]
  (fn [_]
    {:status 200
     :body "{}"}))

(def api-routes
  ["/api/something" :awesome])

(def app
  (-> (constantly {:status 404, :body "not found"})
      (bhandler/make-bidi-handler pwa-routes #'pwa-route-handler)
      (bhandler/make-bidi-handler api-routes #'api-route-handler)
      wrap-base-url
      (resources/wrap-resource "public")))
