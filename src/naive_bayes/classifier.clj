(ns naive-bayes.classifier
  (:require [naive-bayes.analyzer :as analyzer])
  (:import [java.lang Math]))

(defn ^:export predict [text spam-freqs ham-freqs doc-label-probabilities]
  (let [text-word-list (analyzer/analyze text)
        weights (map
                 (fn [word]
                   ;; inc -- Laplace smoothing
                   {:spam-score (Math/log
                                 (inc (get spam-freqs word 0)))
                    :ham-score (Math/log
                                (inc (get ham-freqs word 0)))
                    :value word})
                 text-word-list)
        prediction
        (reduce
         (fn [{:keys [spam ham]} {:keys [spam-score ham-score]}]
           {:spam (+ spam spam-score)
            :ham (+ ham ham-score)})
         doc-label-probabilities
         weights)]
    {:prediction-result (first
                         (reduce
                          (fn [[_ va :as a] [_ vb :as b]]
                            (if (> va vb) a b))
                          prediction))
     :prediction prediction
     :weights weights}))

#_(predict "sample text"
           {"sample" 2}
           {"text" 1}
           {:spam 0
            :ham 0})
