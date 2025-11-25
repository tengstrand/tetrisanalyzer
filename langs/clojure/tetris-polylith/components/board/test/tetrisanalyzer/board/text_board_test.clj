(ns tetrisanalyzer.board.text-board-test
  (:require [clojure.test :refer :all]
            [tetrisanalyzer.board.text-board :as text-board]))

(def board [[0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 6 0 0 0 0 0 0]
            [0 0 6 6 6 0 0 0 0 0]])

(def text-board ["----------"
                 "----------"
                 "---T------"
                 "--TTT-----"])

(deftest text-board->board--test
  (is (= board
         (text-board/text-board->board text-board))))

(deftest board->text-board--test
  (is (= text-board
         (text-board/board->text-board board))))
