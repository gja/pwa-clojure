(ns pwa-clojure.title)

(defn title-for [handler data]
  (case handler
    :home-page "Super Movies"
    :movie-page "Movie Page"
    "Super Movies"))
