(ns tetrisanalyzer.board-test
  (:require [expectations :refer :all]))

(use '[tetrisanalyzer.board])
(use '[tetrisanalyzer.piece])
(use '[tetrisanalyzer.file])

(def test-board (!! "#------#"
                    "#------#"
                    "#------#"
                    "#--S---#"
                    "#--SJ--#"
                    "########"))

(expect (str "#------#\n"
             "#------#\n"
             "#------#\n"
             "#--S---#\n"
             "#--SJ--#\n"
             "########")
        (board->str test-board 8))

(expect [9 0 1 2 3 4 5 6 7 8 0 0 9] (str->row "#-IZSJLTOx--#"))

(expect [21 7 22 7 23 7 32 7]
  (boardpiece 1 2 7 10 [[0 0][1 0][2 0][1 1]]))

(expect [9 0 0 0 0 0 0 9] (empty-line 8))

(expect [9 9 9 9 9 9 9 9] (bottom-line 8))

(expect (!! "#------#"
            "#--LLL-#"
            "#--L---#"
            "#------#"
            "#------#"
            "########")
  (set-piece-on-board 8 (empty-board) 3 1 5 0))

;;(expect is-piece-free

;; set-piece-on-board 8 (empty-board) 2 2 6 1
