(ns task-graph.core
  (:require [clojure.set :as sets]))

(def test-data {"b" #{"a"} "k" #{"a", "c"} "c" #{"m"} "h" #{"b"} "j" #{"b"} "i" #{"b" "k"} "d" #{"c"} "e" #{"c"}
                "f" #{"e"} "g" #{"d" "e"} "l" #{"g" "e"} "n" #{}})

(defn acc-set "Adds new-value to map acc for key." [acc key new-value] (assoc acc key (conj (acc key #{}) new-value)))

(defn process-entry "Adds a dependency graph node's information to map acc." [acc [key val]]
  (if (empty? val) (acc-set acc :singles key)
                   (assoc (acc-set acc :dependers key) :dependencies (sets/union (:dependencies acc) val))))

(defn node-by-types "Summarizes a dependency graph into sets by node type." [inputs] (reduce process-entry {} inputs))

(defn -execution-list "Main task list calculation function." [tasklist processed unprocessed dependers-map]
  (if (not (empty? unprocessed))
    (let [processable     (filter #(empty? (sets/difference (get dependers-map %) processed)) unprocessed)
          new-processed   (sets/union processed processable)
          new-unprocessed (sets/difference unprocessed processable)
          tlist           (into tasklist processable)]
      (recur tlist new-processed new-unprocessed dependers-map))
    tasklist))

(defn execution-list "Summarize graph; find start nodes; calculate executable task list itself." [dependers-map]
  (let [categorised (node-by-types dependers-map)
        starts      (sets/union (:singles categorised) (sets/difference (:dependencies categorised) (:dependers categorised)))]
    (-execution-list (into [] starts) starts (:dependers categorised) dependers-map)))
