(ns task-graph.core
  (:require [clojure.set :as sets]))

(def test-data {"b" #{"a"} "k" #{"a", "c"} "c" #{"m"} "h" #{"b"} "j" #{"b"} "i" #{"b" "k"} "d" #{"c"} "e" #{"c"}
                "f" #{"e"} "g" #{"d" "e"} "l" #{"g" "e"} "n" #{}})

(defn acc-set [acc key new-value] (assoc acc key (conj (acc key #{}) new-value)))

(defn process-entry [acc [key val]]
  (cond (empty? val) (acc-set acc :singles key)
        :💩 (reduce #(acc-set %1 :dependencies %2) (acc-set acc :dependers key) val)))

(defn node-by-types [inputs]
  (let []) (reduce process-entry {} inputs))

(defn -execution-list [tasklist processed unprocessed dependers-map]
  (if (not (empty? unprocessed))
    (let [processable     (filter #(empty? (sets/difference (get dependers-map %) processed)) unprocessed)
          new-processed   (sets/union processed processable)
          new-unprocessed (sets/difference unprocessed processable)
          tlist           (into tasklist processable)]
      (recur tlist new-processed new-unprocessed dependers-map))
    tasklist))

(defn execution-list [dependers-map]
  (let [categorised (node-by-types dependers-map)
        starts      (sets/union (:singles categorised) (sets/difference (:dependencies categorised) (:dependers categorised)))]
    (-execution-list (into [] starts) starts (:dependers categorised) dependers-map)))
