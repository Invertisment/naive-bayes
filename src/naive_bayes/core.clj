(ns naive-bayes.core
  (:require [naive-bayes.files :as files]
            [naive-bayes.analyzer :as analyzer]
            [clojure.pprint :as pprint])
  (:gen-class))

(def evaluation-dataset-size 500)

(defn read-emails []
  (println "Reading data files.")
  (let [spam (analyzer/process (drop evaluation-dataset-size (files/read-dirs files/spam-dirs)))
        ham (analyzer/process (drop evaluation-dataset-size (files/read-dirs files/ham-dirs)))]
    {:spam spam
     :ham ham
     :both {:freqs (apply merge-with + (map :freqs [spam ham]))
            :token-count (reduce + (map :token-count [spam ham]))
            :doc-count (reduce + (map :doc-count [spam ham]))
            :vocabulary (apply into (map :vocabulary [spam ham]))}}))

#_(read-emails)

(defn naive-bayes
  ([]
   (let [analyzed (read-emails)
         both-message-count (-> analyzed :both :doc-count)
         doc-label-probabilities {:spam (java.lang.Math/log
                                         (/
                                          (-> analyzed :spam :doc-count)
                                          both-message-count))
                                  :ham (java.lang.Math/log
                                        (/
                                         (-> analyzed :ham :doc-count)
                                         both-message-count))}
         spam-word-count (-> analyzed :spam :token-count)
         ham-word-count (-> analyzed :spam :token-count)]
     (fn [text]
       (predict
        text
        (-> analyzed :spam :freqs)
        (-> analyzed :ham :freqs)
        doc-label-probabilities))))
  ([& texts]
   (let [classify (naive-bayes)]
     (map (juxt classify identity) texts))))

(defn test-accuracy [model]
  (let [classify model
        spams (map (comp :prediction-result classify) (take evaluation-dataset-size (files/read-dirs files/spam-dirs)))
        hams (map (comp :prediction-result classify) (take evaluation-dataset-size (files/read-dirs files/ham-dirs)))
        spam-freqs (frequencies spams)
        ham-freqs (frequencies hams)
        spam-accuracy (/ (:spam spam-freqs) evaluation-dataset-size)
        ham-accuracy (/ (:ham ham-freqs) evaluation-dataset-size)]
    {:spam-accuracy (double spam-accuracy)
     :ham-accuracy (double ham-accuracy)
     :average-accuracy (double (/ (+ spam-accuracy ham-accuracy) 2))}))

(defn interactive [classify]
  (println "Interactive mode. Please input text.")
  (loop []
    (pprint/pprint (classify (read-line)))
    (recur)))

(defn -main [& flags]
  (let [flags (set flags)
        model (naive-bayes)]
    (cond
      (contains? flags "-p") (pprint/pprint (test-accuracy model))
      :else (do
              (println "For performance measurement pass -p flag")
              (interactive model)))))

#_(naive-bayes
   "Hello world"
   "Takes a function of no args, presumably with side effects, and returns an infinite (or length n if supplied) lazy sequence of calls to it")
