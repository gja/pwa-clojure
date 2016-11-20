(def navigate #?(:cljs actions/navigate :clj (constantly nil)))
(def download-character #?(:cljs actions/download-character :clj (constantly nil)))

(rum/defc pwa-link [{:keys [href] :as params} & children]
  (into [:a (assoc params :on-click #(navigate % href))]
        children))

(rum/defc main-navigation []
  [:nav#main-navigation
   [:ul.main-navigation
    [:li [:a {:href "#hide-navigation" :class "toggle on"} [:span]]]
    [:li (pwa-link {:href "/"} "Home")]
    [:li (pwa-link {:href "/character/583"} "Jon Snow")]
    [:li (pwa-link {:href "/character/1303"} "Daenerys Targaryen")]
    [:li (pwa-link {:href "/character/529"} "Jamie Lannister")]]])

(rum/defc home-component [{:keys [characters]}]
  [:section.characters-grid
   [:ul {}
    (map (fn [{:keys [id name image]}]
           [:li.character {}
            [:figure.character-image-container
             [:img {:src image}]]
            [:div.character-details
             [:h3.character-name (pwa-link {:href (str "/character/" id)} name)]
             [:button.character-download {:on-click #(download-character id)} "Download"]]])
         characters)]])


(comment clj)
(defn layout [component title seo-fields]
  {:headers {"Content-Type" "text/html"}
   :body
   (html5
    [:head
     [:title title]
     [:link {:rel "stylesheet" :href "/css/main.css"}]
     [:link {:rel "manifest" :href "/manifest.json"}]
     (seq seo-fields)
     (comment lots of stuff for mobile)]
    [:body
     [:div.container
      [:div#header-container
       (rum/render-html (components/main-navigation))]
      [:header
       [:a.toggle {:href "#main-navigation" :title "menu"} [:span]]
       [:h1.title "PWA of Thrones"]]
      [:div#container (rum/render-html component)]]
     (include-js "/js/main.js")])})


(comment cljs)
(defn load-app []
  (when-not @app-loaded
    (reset! app-loaded true)
    (rum/mount (reactive-component) (get-container "container"))
    (rum/mount (components/main-navigation) (get-container "header-container"))))
