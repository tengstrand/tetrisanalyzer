(ns tetrisanalyzer.board.clear-rows-test
  (:require [clojure.test :refer :all]
            [tetrisanalyzer.board.interface :as board]))

(deftest clear-two-rows
  (is (= [[0 0 0 0 0 0 0 0 0 0]
          [0 0 0 0 0 0 0 0 0 0]
          [0 0 0 0 0 0 0 0 0 0]
          [0 0 0 0 0 0 0 0 0 0]
          [1 1 1 1 1 1 0 0 1 1]
          [1 0 1 1 1 1 1 1 1 1]]
         (board/clear-rows [[0 0 0 0 0 0 0 0 0 0]
                            [0 0 0 0 0 0 0 0 0 0]
                            [1 1 1 1 1 1 1 1 1 1]
                            [1 1 1 1 1 1 0 0 1 1]
                            [1 0 1 1 1 1 1 1 1 1]
                            [1 1 1 1 1 1 1 1 1 1]]))))
