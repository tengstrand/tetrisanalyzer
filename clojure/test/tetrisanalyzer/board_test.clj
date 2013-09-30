(ns tetrisanalyzer.board-test
  (:require [expectations :refer :all]
            [tetrisanalyzer.board :refer :all]))

(use '[tetrisanalyzer.piece])

;; Should create a board with the default size 6x5.
(expect 30 (count (:dots (empty-board))))

;; Should create an empty vector with the length (width * height).
(expect [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0] (empty-dots 5 4))

;; Should create a 6x5 board.
(expect
  {:width 6
   :height 5
   :dots [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :pieces [[]
            [[0 1 1 1 2 1 3 1] [0 1 6 1 12 1 18 1]]
            [[0 2 1 2 7 2 8 2] [1 2 6 2 7 2 12 2]]
            [[1 3 2 3 6 3 7 3] [0 3 6 3 7 3 13 3]]
            [[0 4 1 4 2 4 8 4] [0 4 1 4 6 4 12 4] [0 4 6 4 7 4 8 4] [1 4 7 4 12 4 13 4]]
            [[0 5 1 5 2 5 6 5] [0 5 6 5 12 5 13 5] [2 5 6 5 7 5 8 5] [0 5 1 5 7 5 13 5]]
            [[0 6 1 6 2 6 7 6] [0 6 6 6 7 6 12 6] [1 6 6 6 7 6 8 6] [1 6 6 6 7 6 13 6]]
            [[0 7 1 7 6 7 7 7]]
            []]} (empty-board 6 5))

;; A board row should be converted to a string representation.
(expect "#--ISJ-#" (row->str [0 0 1 3 4 0]))

(def test-board (empty-board 6 5 (assoc (empty-dots) 1 2 4 3 10 3)))


;; Should convert a board to a string representation.
(expect "#-Z--S-#\n#----S-#\n#------#\n#------#\n#------#\n########"
        (board->str test-board))

;; ................................skriv kommentar!
(expect {:width 6
         :height 5
         :dots [0 2 0 0 3 0 2 2 0 0 3 0 2 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
         :pieces [[] [[0 1 1 1 2 1 3 1] [0 1 6 1 12 1 18 1]] [[0 2 1 2 7 2 8 2] [1 2 6 2 7 2 12 2]]
                  [[1 3 2 3 6 3 7 3] [0 3 6 3 7 3 13 3]]
                  [[0 4 1 4 2 4 8 4] [0 4 1 4 6 4 12 4] [0 4 6 4 7 4 8 4] [1 4 7 4 12 4 13 4]]
                  [[0 5 1 5 2 5 6 5] [0 5 6 5 12 5 13 5] [2 5 6 5 7 5 8 5] [0 5 1 5 7 5 13 5]]
                  [[0 6 1 6 2 6 7 6] [0 6 6 6 7 6 12 6] [1 6 6 6 7 6 8 6] [1 6 6 6 7 6 13 6]]
                  [[0 7 1 7 6 7 7 7]] []]}
  (set-piece test-board 2 1))

;; Should convert a row in a bord to a row of dots.
(expect [0 1 2 3 4 5 6 7 8 0 0] (str->row "#-IZSJLTOx--#"))

(def rows ["#---I--#"
 "#-T----#"])

(str->row "#---#")

(vec (flatten (map #(str->row %) rows)))

(empty-board rows)

;; todo: ny "konstuktor" för board som skickar in #---------# -format!

