(ns clj-flow-graph.core-test
  (:require [clojure.test :refer :all]
            [clj-flow-graph.core :refer :all]
            [clj-flow-graph.algo :as algo]))

(deftest max-flow-single-edge
  (testing "max-flow over a single edge"
    (let [graph (flow-graph [:s :t 10])
          flow (algo/max-flow graph :s :t)]
      (is (= flow 10)))))

(deftest max-flow-two-edge
  (testing "max-flow over 2 edges"
    (let [graph (flow-graph [:s :o 10] [:o :t 2])
          flow (algo/max-flow graph :s :t)]
      (is (= flow 2)))))

(deftest max-flow-basic-test
  (testing "A basic max-flow example."
    (let [graph (flow-graph [:s :o 3] [:s :p 3] [:o :p 2] [:o :q 3]
                         [:p :r 2] [:r :t 3] [:q :r 4] [:q :t 2])
          flow (algo/max-flow graph :s :t)]
      (is (= flow 5)))))

(deftest max-flow-potentially-pathological
  (testing "If our algorithm is... unlucky, this may time out."
    (let [graph (flow-graph [:s :a 50000] [:s :c 50000]
                         [:a :b 1]     [:b :c 1]
                         [:a :t 50000] [:c :t 50000])
          flow (algo/max-flow graph :s :t)]
      (is (= flow 100000)))))

(deftest max-flow-good-example
  (testing "Just an example I like."
    (let [graph (flow-graph [:s :a 5] [:s :b 7] [:s :c 6]
                         [:a :d 4] [:a :e 3] [:b :e 4]
                         [:b :f 1] [:c :f 5] [:d :t 3]
                         [:e :t 7] [:f :e 4] [:f :t 6])
          flow (algo/max-flow graph :s :t)]
      (is (= flow 15)))))



