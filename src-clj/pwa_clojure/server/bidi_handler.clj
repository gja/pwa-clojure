(ns pwa-clojure.server.bidi-handler
  (:require [bidi.bidi :refer :all]
            [bidi.ring :refer :all]))

(defn make-bidi-handler
  "Create a Ring handler from the route definition data
  structure. Matches a handler from the uri in the request, and invokes
  it with the request as a parameter."
  ([f route handler-fn]
   (assert route "Cannot create a Ring handler with a nil Route(s) parameter")
   (fn [{:keys [uri path-info] :as req}]
     (let [path (or path-info uri)
           {:keys [handler route-params] :as match-context}
           (match-route* route path req)]
       (if handler
         (request
          (handler-fn handler)
          (-> req
              (update-in [:params] merge route-params)
              (update-in [:route-params] merge route-params))
          (apply dissoc match-context :handler (keys req)))
         (f req)))))
  ([f route] (make-handler route identity)))
