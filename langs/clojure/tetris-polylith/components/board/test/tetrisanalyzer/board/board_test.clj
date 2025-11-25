(ns tetrisanalyzer.board.board-test
  (:require [clojure.test :refer :all]
            [tetrisanalyzer.piece.interface :as piece]
            [tetrisanalyzer.board.board :as board]))

(def empty-board-6x4 [[0 0 0 0 0 0]
                      [0 0 0 0 0 0]
                      [0 0 0 0 0 0]
                      [0 0 0 0 0 0]])

(deftest empty-board-6x4-test
  (is (= empty-board-6x4
         (board/empty-board 6 4))))

(deftest set-piece-test
  (let [T piece/T
        piece-t (piece/piece T 0)
        x 2
        y 1]
    (is (= [[0 0 0 0 0 0]
            [0 0 T T T 0]
            [0 0 0 T 0 0]
            [0 0 0 0 0 0]]
           (board/set-piece empty-board-6x4 T x y piece-t)))))
