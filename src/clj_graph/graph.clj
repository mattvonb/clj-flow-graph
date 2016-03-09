(ns clj-graph.graph)

(defrecord Edge [source sink])

;; Constructor function.
(defn edge [source sink]
   (Edge. source sink))

;; returns the reverse edge for +e+
;; if x is an edge from :a to :b
;; then the reverse edge for x is an edge from :b to :a
(defn re [e]
  (edge (:sink e) (:source e)))

;; +edges+ maps a node to the set of all edges for which it is the source
;; +capacities+ maps an edge to its integer capacity
;; +flows+ maps an edge to the integer flow of that edge
(defrecord Graph [edges capacities flows])

;; all the edges with the given source
(defn edges [graph source]
  (get (:edges graph) source))

;; +source+ must be (:source edge)
(defn- assoc-edge [graph source edge]
  (if-let [edges (edges graph source)]
    (assoc-in graph [:edges source] (conj edges edge))
    (assoc-in graph [:edges source] #{edge})))

;; add an edge to the graph from +source+ to +sink+ with capacity +capacity+
(defn add-edge [graph source sink capacity]
  (if (= source sink)
    (throw (RuntimeException. (str "source " source " cannot equal sink " sink)))
    (let [e (edge source sink)
          re (re e)]
      (-> graph
          (assoc-edge source e)
          (assoc-edge sink re)
          (assoc-in [:capacities e] capacity)
          (assoc-in [:capacities re] 0)
          (assoc-in [:flows e] 0)
          (assoc-in [:flows re] 0)))))

;; Constructor function.
;; args are [source sink capacity] triples that define edges.
(defn graph
  ([]
   (Graph. {} {} {}))
  ([& edges]
   (reduce (fn [g edge] (apply (partial add-edge g) edge))
           (graph)
           edges)))

;; the flow over edge +edge+
(defn flow [graph edge]
  (get (:flows graph) edge))

;; the capacity of edge +edge+
(defn capacity [graph edge]
  (get (:capacities graph) edge))

;; returns the unused capacity available over +edge+
(defn residual-capacity [graph edge]
  (- (capacity graph edge) (flow graph edge)))

;; increment the flow over +edge+ by +flow-inc+
(defn add-flow-in [graph edge flow-inc]
  (-> graph
      (assoc-in [:flows edge] (+ (flow graph edge) flow-inc))
      (assoc-in [:flows (re edge)] (- (flow graph (re edge)) flow-inc))))


  
