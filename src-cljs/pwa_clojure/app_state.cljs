(ns pwa-clojure.app-state)

(defn- update-title [title]
  (set! (-> js/window .-document .-title) title))

(defn- replace-state [url]
  (.replaceState (-> js/window .-history) nil nil url))

(defn- push-state [url]
  (.pushState (-> js/window .-history) nil nil url))

(defonce app-state (atom {}))

(add-watch app-state :set-title-and-url
           (fn [key atom old new]
             (when (not= (:title old) (:title new))
               (update-title (:title new)))
             (when (not= (:url old) (:url new))
               (if (:url old)
                 (push-state (:url new))
                 (replace-state (:url new))))))
