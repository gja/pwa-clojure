(def data-cache-name "pwa-clojure-data")

(defn load-data [handler args callback]
  (let [route (apply bidi/path-for routes/api-routes handler (flatten (vec args)))]
    (if (and js/window.caches js/fetch)
      (-> (cached-get route) (.then callback))
      (fallback-get route callback))))

(defn- cached-get [route]
  (-> (.match js/window.caches route)
      (.then
       (fn [response]
         (if response
           (response->clj response)
           (-> (js/fetch route)
               (.then
                (fn [response]
                  (cache-response route response)
                  (response->clj response)))))))))

(defn- cache-response [route response]
  (js/console.log "Caching Data" route)
  (let [clone (.clone response)]
    (-> js/caches
        (.open data-cache-name)
        (.then #(.put % route clone)))))

(defn- response->clj [response]
  (-> response .json (.then #(js->clj % :keywordize-keys true))))
