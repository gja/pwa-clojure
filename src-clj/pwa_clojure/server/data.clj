(ns pwa-clojure.server.data
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.string :as str]))

(defn- fix-character [{:keys [url name] :as character}]
  (assoc character
         :id (-> url (str/split #"/") last)
         :name (if (not= name "")
                 name
                 "Unknown")))

(defn parse-json [x]
  (json/parse-string x keyword))

(defn- characters [args]
  {:characters
   (->> (http/get "http://anapioficeandfire.com/api/characters?pageSize=100")
        :body
        parse-json
        (map fix-character)
        (map #(select-keys % [:id :name])))})

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
