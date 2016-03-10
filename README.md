The clj-flow-graph.core namespace provides an implementation of a directed graph with weighted edges.

The clj-flow-graph.algo namespace provides an implementation of an algorithm for finding the max flow of a graph.

E.g.:

    (require '[clj-flow-graph.core :as g]
             '[clj-flow-graph.algo :as algo])
    
    (-> (g/flow-graph [:s :o 3]
                      [:s :p 3]
                      [:o :p 2]
                      [:o :q 3]
                      [:p :r 2]
                      [:r :t 3]
                      [:q :r 4]
                      [:q :t 2])
        (algo/max-flow :s :t))
    ;; 5