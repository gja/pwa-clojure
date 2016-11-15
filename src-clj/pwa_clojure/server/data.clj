(ns pwa-clojure.server.data
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.string :as str]))

(defn- fix-character [{:strs [url name] :as character}]
  (assoc character
         "id" (-> url (str/split #"/") last)
         "name" (if (not= name "")
                  name
                  "Unknown")))

(defn- characters [args]
  (->> (http/get "http://anapioficeandfire.com/api/characters?pageSize=100")
       :body
       json/parse-string
       (map fix-character)
       (map #(select-keys % ["id" "name"]))))

(defn- character [{:keys [character-id]}]
  (->> (http/get (str "http://anapioficeandfire.com/api/characters/" character-id))
       :body
       json/parse-string
       fix-character))

(defn load-data [handler args]
  (case handler
    :characters (characters args)
    :character (character args)))
