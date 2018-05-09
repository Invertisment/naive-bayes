(defproject naive-bayes "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.521"]]
  :cljsbuild {:builds
              [{:source-paths ["src"]
                :compiler
                {:output-to "public/js/main.js"
                 :optimizations :whitespace
                 :pretty-print true}}]}
  :main naive-bayes.core
  :plugins [[lein-cljsbuild "1.1.7"]])
