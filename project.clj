(defproject pwa-clojure "0.1.1-SNAPSHOT"
  :description "A simple example of to build a PWA app with clojurescript"
  :source-paths ["src-clj"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.293"]
                 [bidi "2.0.13"]
                 [hiccup "1.0.4"]
                 [rum "0.10.7"]]
  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-ring "0.8.7"]]
  :cljsbuild
  {:builds [{:source-paths ["src-cljs"]
             :compiler {:output-to "resources/public/js/main.js"
                        :main pwa-clojure.hello
                        :optimizations :advanced
                        :pretty-print true}}
            {:source-paths ["src-svc"]
             :compiler {:output-to "resources/public/service-worker.js"
                        :main pwa-clojure.service-worker
                        :optimizations :advanced
                        :pretty-print true}}]}
  :ring {:handler pwa-clojure.app/app})
