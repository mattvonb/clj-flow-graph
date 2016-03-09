(ns clj-graph.algo
  (:require [clj-graph.graph :as g]))

;; +prev+ is a map of a node to an edge for which that node is its sink.
;; +sink+ is a node
;; Walk backward from +sink+ in +prev+, storing each edge in a list.
;; @TODO -- Seems non-idiomatic. Is there a way too do this without using loop?
(defn- to-path [prev sink]
  (loop [sink sink
         path '()
         e (get prev sink)]
    (if (nil? e)
      path
      (recur (:source e) (conj path e) (get prev sink)))))

;; A breadth-first search from +source+ to +sink+ whereby each edge traversed
;; must have a non-zero residual capacity.
;; Returns the path from +source+ to +sink+.
;; @TODO -- This implementation is ugly and seems non-idiomatic.
(defn bfs [graph source sink]
  (loop [prev {source nil}
         visited #{source}
         q (conj clojure.lang.PersistentQueue/EMPTY source)]
    (let [cur (peek q)
          q (pop q)

          ;; successors must have non-zero residual capacity
          ;; and be not yet visited
          nexts (filter #(and (> (g/residual-capacity graph %) 0)
                              (not (contains? visited %)))
                        (g/edges graph cur))
          
          prev (into prev (map #(assoc {} (:sink %) %) nexts))]

      ;; we're done if we found our destination, +sink+ or have explored the
      ;; entire graph
      (cond
        (contains? prev sink) (to-path prev sink)
        (empty? q) nil
        :else (recur prev
                     (into visited (map #(:sink %) nexts))
                     (into q (map #(:sink %) nexts)))))))
          

;; Returns a path from +source+ to +sink+ that has a non-zero flow capacity
(defn- find-augmenting-path [graph source sink]
  (if (= source sink)
    nil
    (bfs graph source sink)))
    
;; augments the flow over +path+ in the graph
(defn- augment-path-in [graph path]
  ;; get the smallest residual capacity across an edge in +path+
  ;; then add that amount of flow to all edges in that path.
  (let [flow (apply min (map #(g/residual-capacity graph %) path))]
    (reduce (fn [g e] (g/add-flow-in g e flow))
            graph
            path)))

;; finds the flow out of node +source+
(defn flow [graph source]
  (reduce (fn [sum edge] (+ sum (g/flow graph edge)))
          0
          (g/edges graph source)))

;; returns the max flow from +source+ to +sink+
(defn max-flow [graph source sink]
  (loop [graph graph
         path (find-augmenting-path graph source sink)]
    (if (nil? path)
      (flow graph source)
      (recur (augment-path-in graph path)
             (find-augmenting-path graph source sink)))))
      

          

    
         

