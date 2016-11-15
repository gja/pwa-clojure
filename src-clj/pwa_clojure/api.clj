(ns pwa-clojure.api
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.string :as str]))

;; This namespace is super simple

(defn- api-response [body]
  {:satus 200
   :headers {"Content-Type" "application/json"}
   :body (json/encode body)})

(defn- fix-string [{:strs [url name] :as character}]
  (assoc character
         "id" (-> url (str/split #"/") last)
         "name" (if (not= name "")
                  name
                  "Unknown")))

(defn- characters [request]
  (->> (http/get "http://anapioficeandfire.com/api/characters?pageSize=100")
       :body
       json/parse-string
       (map fix-string)
       (map #(select-keys % ["id" "name"]))
       api-response))

(defn- character [{:keys [route-params] :as request}]
  (->> (http/get (str "http://anapioficeandfire.com/api/characters/" (:character-id route-params)))
       :body
       json/parse-string
       fix-string
       api-response))

(defn handler [handler]
  (case handler
    :characters characters
    :character character))
