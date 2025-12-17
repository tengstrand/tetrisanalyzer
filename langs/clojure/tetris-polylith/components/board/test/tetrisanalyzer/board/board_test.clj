(ns tetrisanalyzer.board.board-test
  (:require [clojure.test :refer :all]
            [tetrisanalyzer.piece.interface :as piece]
            [tetrisanalyzer.board.board :as board]))

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

(deftest set-piece-test
  (let [T piece/T
        piece-t (piece/piece T 0)
        x 2
        y 13]
    (is (= [[0 0 0 0 0 0 0 0 0 0]
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
            [0 0 T T T 0 0 0 0 0]
            [0 0 0 T 0 0 0 0 0 0]]
           (board/set-piece empty-board T x y piece-t)))))
