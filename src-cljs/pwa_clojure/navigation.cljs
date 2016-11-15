(ns pwa-clojure.navigation
  (:require [bidi.bidi :as bidi]
            [pwa-clojure.pages :as pages]
            [pwa-clojure.routes :as routes]
            [pwa-clojure.app-state :as app-state]))

(defn- fetch-data [handler route-params]
  {:name "Character of the Year!"})

(defn ^:export move-to-page [path]
  (let [{:keys [handler route-params]} (bidi/match-route routes/pwa-routes path)
        data (fetch-data handler route-params)]
    (reset! app-state/app-state {:handler handler
                                 :data data
                                 :url path
                                 :title (pages/title handler data)})))

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
