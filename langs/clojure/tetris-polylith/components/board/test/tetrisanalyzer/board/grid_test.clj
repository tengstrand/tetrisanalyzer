(ns tetrisanalyzer.board.grid-test
  (:require [clojure.test :refer :all]
            [tetrisanalyzer.board.grid :as grid]))

(deftest grid-row->row
  (is (= [0 0 1 0 0 1 1 0]
         (grid/row '--x--xx-))))

(deftest grid->board
  (is (= [[0 0 1 0 0 1 1 0]
          [1 0 0 0 0 0 0 1]]
         (grid/board ['--x--xx-
                      'x------x]))))
