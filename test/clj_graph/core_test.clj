(ns clj-graph.core-test
  (:require [clojure.test :refer :all]
            [clj-graph.graph :as g]
            [clj-graph.algo :refer :all]))

(deftest max-flow-tiny-test
  (testing "tiny max flow example"
    (let [graph (g/graph [:s :t 10])
          flow (max-flow graph :s :t)]
      (prn "flow:" flow)
      (is (= flow 10)))))

(deftest max-flow-basic-test
  (testing "basic max flow example"
    (let [graph (g/graph [:s :o 3] [:s :p 3] [:o :p 2] [:o :q 3]
                         [:p :r 2] [:r :t 3] [:q :r 4] [:q :t 2])
          flow (max-flow graph :s :t)]
      (prn "flow:" flow)
      (is (= flow 5)))))
