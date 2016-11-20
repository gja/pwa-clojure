(.addEventListener js/self "fetch" #(.respondWith % (fetch-event %)))

(defn- fetch-event [e]
  (js/console.log "[ServiceWorker] Fetch" (-> e .-request .-url))
  (let [request (.-request e)
        url (-> request .-url url/url)]
    (case (:host url)
      "pwa-clojure.staging.quintype.io"
      (fetch-page-or-cached (:path url) e)

      (fetch-cached request))))

(defn- fetch-page-or-cached [path e]
  (cond
    (bidi/match-route routes/pwa-routes path)
    (return-shell e)

    :else
    (fetch-cached (.-request e))))

(defn- return-shell [e]
  (js/console.log "[ServiceWorker] Cached Page")
  (-> js/caches
      (.match "/shell.html")
      (.then (fn [response]
               (or response (js/fetch (.-request e)))))))

(defn- fetch-cached [request]
  (-> js/caches
      (.match request)
      (.then (fn [response]
               (or response (js/fetch request))))))
