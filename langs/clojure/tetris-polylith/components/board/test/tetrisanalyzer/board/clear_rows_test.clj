(ns tetrisanalyzer.board.clear-rows-test
  (:require [clojure.test :refer :all]
            [tetrisanalyzer.board.clear-rows :as sut]))

(deftest clear-two-rows
  (is (= [[0 0 0 0 0 0 0 0 0 0]
          [0 0 0 0 0 0 0 0 0 0]
          [0 0 0 0 0 0 0 0 0 0]
          [0 0 0 0 0 0 0 0 0 0]
          [1 1 1 1 1 1 0 0 1 1]
          [1 0 1 1 1 1 1 1 1 1]]
         (sut/clear-rows [[0 0 0 0 0 0 0 0 0 0]
                          [0 0 0 0 0 0 0 0 0 0]
                          [1 1 1 1 1 1 1 1 1 1]
                          [1 1 1 1 1 1 0 0 1 1]
                          [1 0 1 1 1 1 1 1 1 1]
                          [1 1 1 1 1 1 1 1 1 1]]))))
