(ns clj-graph.algo
  (:require [clj-graph.graph :as g]))

;; walk backward from node +t+ in +prev+, storing each edge in a list
(defn to-path [prev t]
  (loop [t t
         path '()
         e (get prev t)]
    (if (nil? e)
      path
      (recur (:source e) (conj path e) (get prev t)))))
    

;; breadth-first search from +s+ to +t+.
;; returns the path from +s+ to +t+.
(defn bfs [graph s t]
  (loop [prev {s nil}
         visited #{s}
         q (conj clojure.lang.PersistentQueue/EMPTY s)]
    (let [cur (peek q)
          q (pop q)

          ;; any potential successors have to have capacity and
          ;; not have been visited
          nexts (filter #(and (> (g/residual-capacity graph %) 0)
                              (not (contains? visited %)))
                        (g/edges graph cur))
          
          prev (into prev (map #(assoc {} (:sink e) e) nexts))]

      ;; we're done if we found our destination, +t+
      ;; or have explored the entire graph
      (cond
        (contains? prev t) (to-path prev t)
        (empty? q) nil
        :else (recur prev
                     (into visited (map #(:sink %) nexts))
                     (into q (map #(:sink %) nexts)))))))
          

;; returns a path from +s+ to +t+ in +graph+ that
;; has a non-zero flow capacity
(defn find-path [graph s t]
  (if (= s t)
    nil
    (bfs [graph s t])))
    
;; adds the augmenting path +path+ to flow graph +graph+
(defn- update-path-in [graph path]
    (let [flow (apply min (map #(- (:capacity %) (g/flow graph %)) path))]
      (reduce (fn [g e] (-> g (assoc-in [:flows e] )
          
          (loop [graph graph
      

;; finds the flow in graph +graph+ out of node +s+
(defn- flow [graph s]
  (reduce + 0 (g/edges graph s)))

;; returns the max flow from +s+ to +t+ in +graph+
(defn max-flow [graph s t]
  (loop [graph graph
         path (find-path graph s t)]
    (if (nil? path)
      (flow graph s)
      (recur (update-path-in graph path)
             (find-path graph s t)
      

          

    
         

