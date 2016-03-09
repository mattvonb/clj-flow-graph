(ns clj-graph.graph)

(defrecord Edge [source sink capacity])

(defn edge [source sink capacity]
   (Edge. source sink capacity))

(defrecord Graph [edges flows])

(defn graph []
  (Graph. {} {}))

(defn- assoc-edge [graph node edge]
  (if-let [edges (get (:edges graph) node)]
    (assoc-in graph [:edges node] (conj edges edge))
    (assoc-in graph [:edges node] #{edge})))

;; add an edge to the graph from +source+ to +sink+ with capacity +capacity+
(defn add-edge [graph source sink capacity]
  (if (= source sink)
    (throw (RuntimeException. (str "source " source " cannot equal sink " sink)))
    (let [e (edge source sink capacity)
          re (edge sink source 0)]
      (-> graph
          (assoc-edge source e)
          (assoc-edge sink re)
          (assoc-in [:flows e] 0)
          (assoc-in [:flows re] 0)))))

;; all the edges with the given source
(defn edges [graph source]
  (get (:edges graph) source))

;; the flow over edge +edge+
(defn flow [graph edge]
  (get (:flows graph) edge))

;; returns the flow capacity available over +edge+
(defn residual-capacity [graph edge]
  (- (:capacity edge) (flow graph edge)))
  
  
