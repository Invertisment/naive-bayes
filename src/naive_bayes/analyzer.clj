(ns naive-bayes.analyzer
  (:require [clojure.string :as cljstr]))

(defn char-filter [text]
  (cljstr/replace text #"[^a-zA-Z \t\n\r]" ""))

(defn tokenize [text]
  (cljstr/split text #"[^a-zA-Z]"))

(defn remove-stopwords [list]
  (filter (comp not empty?) list))

(defn analyze [text]
  (-> text
      char-filter
      cljstr/lower-case
      tokenize
      remove-stopwords))

(defn process [str-list]
  (let [tokens (map analyze str-list)]
    ;; Consumes less memory and can be parallelized when chunked
    (let [freqs (reduce
                 #(merge-with + %1 %2)
                 (map frequencies tokens))]
      {:freqs freqs
       :token-count (reduce + (map count tokens))
       :doc-count (count tokens)
       :vocabulary (into #{} (keys freqs))})))

#_(analyze "Hello world!!!\tlol\nnew-line")
#_(process ["My name is john" "i like trains" "it is blue"])
