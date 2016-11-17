(ns pwa-clojure.server.data
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.string :as str]))

(defn- fix-character [{:keys [url name] :as character}]
  (assoc character
         :id (-> url (str/split #"/") last)
         :name (if (not= name "")
                 name
                 "Unknown")
         :image (case name
                  "Jon Snow" "https://vignette2.wikia.nocookie.net/gameofthrones/images/4/4c/JonSnowTightened-S4.jpg/revision/latest?cb=20140322183538"
                  "Daenerys Targaryen" "https://vignette2.wikia.nocookie.net/gameofthrones/images/2/28/Dany_finale_s6_winds_of_winter.jpg/revision/latest?cb=20160630224410"
                  "Jamie Lannister" "https://vignette4.wikia.nocookie.net/gameofthrones/images/c/c5/Jaime_s6_Ep08_.jpg/revision/latest?cb=20160616044155"
                  "https://upload.wikimedia.org/wikipedia/en/f/fb/White_Walker-Game_of_Thrones-S02-E10.jpg")))

(defn parse-json [x]
  (json/parse-string x keyword))

(def favorite-characters
  [{:url "characters/583"
    :name "Jon Snow"}
   {:url "characters/1303"
    :name "Daenerys Targaryen"}
   {:url "characters/529"
    :name "Jamie Lannister"}])

(defn- characters [args]
  {:characters
   (->> (http/get "http://anapioficeandfire.com/api/characters?pageSize=47&page=2")
        :body
        parse-json
        (into favorite-characters)
        (map fix-character)
        (map #(select-keys % [:id :name :image])))})

(defn- character [{:keys [character-id]}]
  {:character
   (->> (http/get (str "http://anapioficeandfire.com/api/characters/" character-id))
        :body
        parse-json
        fix-character)})

(defn load-data [handler args]
  (case handler
    :characters (characters args)
    :character (character args)))
