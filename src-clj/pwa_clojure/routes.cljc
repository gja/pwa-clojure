(ns pwa-clojure.routes)

(def pwa-routes
  [""
   {"/" :home-page
    ["/movie/" :movie-id] :movie-page}])

(def api-routes
  ["/api"
   {"/movies" :movies
    ["/movies/" :movie-id] :movie}])
