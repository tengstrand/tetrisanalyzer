(ns tetrisanalyzer.board
  (:require [expectations :refer :all]))

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

(expect (!! "#------#"
            "#--LLL-#"
            "#--L---#"
            "#------#"
            "#------#"
            "########")
  (set-piece-on-board 8 (empty-board) 3 1 5 0))

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


