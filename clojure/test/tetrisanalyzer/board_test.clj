(ns tetrisanalyzer.board-test
  (:require [expectations :refer :all]
            [tetrisanalyzer.board :refer :all]))

(use '[tetrisanalyzer.piece])
(use '[tetrisanalyzer.file])

(expect 30 (count (:dots (new-board))))

(expect [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0] (empty-dots 5 4))

(expect
  {:width 6
   :height 5
   :dots [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]} (new-board 6 5))

;; A board row should be converted to a string representation.
(expect "#--ISJ-#" (row->str [0 0 1 3 4 0]))

(def test-board (!!! "#------#"
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
        (board->str2 test-board 8))

(expect [9 0 1 2 3 4 5 6 7 8 0 0 9] (str->row "#-IZSJLTOx--#"))

(expect [21 7 22 7 23 7 32 7]
  (boardpiece 1 2 7 10 [[0 0][1 0][2 0][1 1]]))

(expect (!!! "#------#"
            "#--LLL-#"
            "#--L---#"
            "#------#"
            "#------#"
            "########")
  (set-piece-on-board2 (empty-board) 8 3 1 5 0))

(expect [9 0 0 0 0 0 0 9] (empty-line 8))

(expect [9 9 9 9 9 9 9 9] (bottom-line 8))

;; ########## Manual tests ##########

;; (defn write-board [board]
;;   (def filename "C:/Source/IDEA/tetrisanalyzer/clojure/target/tetris.txt")
;;   (def board-str (board->str board))
;;   (with-open [w (clojure.java.io/writer filename :append true)]
;;     (.write w (str "\n\n" board-str))))

;;(write-board test-board)
;;(write-board (set-piece test-board 2 0))
;;(write-board test-board3)


