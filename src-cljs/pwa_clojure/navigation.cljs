(ns pwa-clojure.navigation
  (:require [bidi.bidi :as bidi]
            [pwa-clojure.pages :as pages]
            [pwa-clojure.routes :as routes]
            [pwa-clojure.app-state :as app-state]
            [pwa-clojure.client.data :as data]))

(defn ^:export move-to-page
  ([path]
   (move-to-page path (constantly nil)))
  ([path callback]
   (let [{:keys [handler route-params]} (bidi/match-route routes/pwa-routes path)
         [data-handler data-args] (first (pages/data-requirements handler route-params))]
     (data/load-data data-handler data-args
                     (fn [data]
                       (reset! app-state/app-state {:handler handler
                                                    :data data
                                                    :url path
                                                    :title (pages/title handler data)})
                       (callback))))))

(defn- update-title [title]
  (set! js/window.location.title title))

(defn- replace-state [url]
  (.replaceState js/window.history nil nil url))

(defn- push-state [url]
  (.pushState js/window.history nil nil url))

(add-watch app-state/app-state :set-title-and-url
           (fn [key atom old new]
             (when (not= (:title old) (:title new))
               (update-title (:title new)))
             (when (not= (:url old) (:url new))
               (if (:url old)
                 (push-state (:url new))
                 (replace-state (:url new))))))

(set! js/window.onpopstate
      (fn [event]
        (move-to-page js/window.location.pathname)))
