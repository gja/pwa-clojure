(ns pwa-clojure.routes)

(def pwa-routes
  [""
   {["/character/" :character-id] :character-page
    "/" :home-page}])

(def api-routes
  ["/api"
   {["/characters/" :character-id] :character
    "/characters" :characters}])
