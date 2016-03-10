Provides an implementation of a directed graph with weighted edges via the clj-flow-graph.core namespace.

A method for computing the maximum flow from a given source to a given sink in a given graph is provided by the clj-flow-graph.algo namespace.

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