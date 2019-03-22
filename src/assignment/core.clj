(ns assignment.core
  (:require [clojure.math.combinatorics :as combo]))

(defn possible-coinsets
  "only those coin combinations that don't exceed amount in the one-each case
   eg. (possible-coinsets 10 [1 5 10]) => ((1) (5) (10) (1 5)
   excluding (1 10) (5 10) (1 5 10)) because they exceed 10 in the one-each case"
  [amount coinset]
  (filter #(<= (apply + %) amount)
          (mapcat #(combo/combinations coinset %)
                  (range 1 (inc (count coinset))))))

(defn sum-coincount-mask
  "if coinset = [1 5 10],
   and coincount-mask = [1 2 3],
   then the result is (+ (* 1 1) (* 5 2) (* 10 3))"
  [coinset coincount-mask]
  (apply + (map * coinset coincount-mask)))

(defn makes-change?
  "(makes-change? 6 [1 5] [1 1]) => true"
  [amount coinset coincount-mask]
  (= amount (sum-coincount-mask coinset coincount-mask)))

(defn map-coins-to-counts
  [coinset coincount-mask]
  (into {} (map vector coinset coincount-mask)))

(defn possible-coincount-masks
  "1. generate coincount-masks that represent the counts for coins in
      coinset of length n. ie. for n = 3 with coincount-max = 2
   [1 1 1]
   [2 1 1]
   [1 2 1]
   [1 1 2]
   [2 2 1]
   etc...
   [2 2 2]

   2. if applying the last (largest) coincount-mask ie. [2 2 2] for
      the coinset is lower than the amount, then increase then coincoint
      max and recursively call to generate coincount-masks with an
      incrementally higher coincount max, ie. up to
      [3 3 3] if previous max coincount-mask was [2 2 2]."
  ([amount coinset]
   (possible-coincount-masks amount coinset 2))
  ([amount coinset coincount-max]
   (let [coincount-masks (combo/selections (range 1 coincount-max) (count coinset))]
     (if (< (sum-coincount-mask coinset (last coincount-masks)) amount)
       (recur amount coinset (inc coincount-max))
       coincount-masks))))

(defn make-change [amount coinset]
  (filter identity
          (for [coinset' (possible-coinsets amount coinset)
                coincount-mask (possible-coincount-masks amount coinset')]
            (when (makes-change? amount coinset' coincount-mask)
              (map-coins-to-counts coinset' coincount-mask)))))

#_(make-change 6 [1 5 10 25]) ;=> ({1 6} {1 1, 5 1})
#_(make-change 6 [3 4])       ;=> ({3 2})
#_(make-change 6 [1 3 4])     ;=> ({1 6} {3 2} {1 2, 4 1})
#_(make-change 6 [5 7])       ;=> ()
