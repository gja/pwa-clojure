(ns pwa-clojure.routes)

(def pwa-routes
  [""
   {"/" :home-page
    ["/character/" :character-id] :character-page}])

(def api-routes
  ["/api"
   {"/characters" :characters
    ["/characters/" :character-id] :character}])
