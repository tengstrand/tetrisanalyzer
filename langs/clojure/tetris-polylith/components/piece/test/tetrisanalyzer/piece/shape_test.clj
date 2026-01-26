(ns tetrisanalyzer.piece.shape-test
  (:require [clojure.test :refer :all]
            [tetrisanalyzer.piece.shape :as shape]))

(deftest converts-a-piece-shape-grid-to-a-vector-of-xy-cells
  (is (= [[2 0]
          [1 1]
          [2 1]
          [1 2]]
         (shape/shape ['--x-
                       '-xx-
                       '-x--
                       '----]))))
