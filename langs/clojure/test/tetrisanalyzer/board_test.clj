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

(expect [21 7 22 7 31 7 32 7]
  (boardpiece 10 1 2 7 0))

(expect [9 0 0 0 0 0 0 9] (empty-line 8))

(expect [9 9 9 9 9 9 9 9] (bottom-line 8))

(expect (!! "#------#"
            "#--LLL-#"
            "#--L---#"
            "#------#"
            "#------#"
            "########")
  (set-piece-on-board 8 (empty-board) 3 1 5 0))
