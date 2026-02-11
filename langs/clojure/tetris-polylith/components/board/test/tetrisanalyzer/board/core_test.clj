(ns tetrisanalyzer.board.core-test
  (:require [clojure.test :refer :all]
            [tetrisanalyzer.board.core :as board]))

(def empty-board [[0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]])

(deftest empty-board-test
  (is (= empty-board
         (board/empty-board 10 15))))
