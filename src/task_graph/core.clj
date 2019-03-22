(ns task-graph.core
  (:require [clojure.set :as sets]))

(def test-data {"b" #{"a"} "k" #{"a", "c"} "c" #{"m"} "h" #{"b"} "j" #{"b"} "i" #{"b" "k"} "d" #{"c"} "e" #{"c"}
                "f" #{"e"} "g" #{"d" "e"} "l" #{"g" "e"} "n" #{}})

(defn unroll-inputs [inputs]
  (for [i (keys inputs) j (let [deps (get inputs i)] (if (empty? deps) [:singleton] deps))] [j i]))

(defn dependency-dependers-map [unrolled-inputs]
  (reduce
    (fn [target [dependency depender]] (assoc target dependency (conj (get target dependency #{}) depender)))
    {}
    unrolled-inputs))

(defn starts [dependers-map dependencies-map]
  (sets/union (get dependencies-map :singleton #{})
    (sets/difference (->> dependencies-map keys (filter (complement keyword?)) set) (-> dependers-map keys set))))

(defn -execution-list [nof-tasks tasklist processed dependencies-map dependers-map]
  (if (< (count processed) nof-tasks)
    (let [unprocessed-dependers (sets/difference (-> (keys dependers-map) set) processed)
          processable (filter #(empty? (sets/difference (get dependers-map %) processed)) unprocessed-dependers)
          new-processed (sets/union processed processable)
          tlist (into tasklist processable)]
      (recur nof-tasks tlist new-processed dependencies-map dependers-map))
    tasklist))

(defn execution-list [dependencies-map dependers-map]
  (let [start (starts dependers-map dependencies-map)
        all-tasks (->> [dependencies-map dependers-map] (map keys) (map set) (apply sets/union) (filter (complement keyword?)))
        nof-tasks (count all-tasks)]
    (-execution-list nof-tasks (into [] start) start dependencies-map dependers-map)))

(defn setup-ns []
  (do (def unrolled (unroll-inputs test-data))
      (def dependencies-map (dependency-dependers-map unrolled))
      (def start (starts test-data dependencies-map))
      (def resultlist (execution-list dependencies-map test-data))))
