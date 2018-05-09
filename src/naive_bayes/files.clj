(ns naive-bayes.files
  (:require [clojure.java.io :as io]))

(def testing false)

(def data-dirs
  (map
   #(str "data/enron" % "/")
   (range 1 7)))

(def spam-dirs
  (map
   #(str % "spam/")
   data-dirs))

(def ham-dirs
  (map
   #(str % "ham/")
   data-dirs))

(defn read-dirs [dirs]
  (let [list (map
              slurp
              (mapcat
               #(.listFiles (io/file (io/resource %)))
               dirs))]
    (if testing
      (take 5 list)
      list)))

#_(type (read-dirs spam-dirs))
#_(read-emails)
#_(map slurp (.listFiles (read-emails spam-dirs)))
