(comment clojure version)

(defn pwa-page [handler {:keys [route-params]}]
  (let [[data-handler data-args] (first (pages/data-requirements handler route-params))
        data (data/load-data data-handler data-args)
        component (pages/pwa-component handler data)]
    (layout component (pages/title handler data) (seo-fields handler data))))



(comment clojurescript version)

(defn ^:export move-to-page [path callback]
  (let [{:keys [handler route-params]} (bidi/match-route routes/pwa-routes path)
        [data-handler data-args] (first (pages/data-requirements handler route-params))]
    (data/load-data data-handler data-args
                    (fn [data]
                      (reset! app-state/app-state {:handler handler
                                                   :data data
                                                   :url path
                                                   :title (pages/title handler data)})))))
