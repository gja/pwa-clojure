(def app-state/app-state (atom {}))

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




(rum/defc reactive-component < rum/reactive []
  (pages/pwa-component (:handler (rum/react app-state/app-state))
                       (:data (rum/react app-state/app-state))))

(defn ^:export start-cljs-app []
  (navigation/move-to-page (get-current-path) load-app)
  (make-progressive!))
