(ns tetrisanalyzer.board-test
  (:require [expectations :refer :all]
            [tetrisanalyzer.board :refer :all]))

(use '[tetrisanalyzer.piece])
(use '[tetrisanalyzer.file])

(expect 30 (count (:dots (empty-board))))

(expect [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0] (empty-dots 5 4))

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

;; #------#
;; #------#
;; #------#
;; #--S---#
;; #--SJ--#
;; ########
(def test-board (empty-board 6 5 (assoc (empty-dots) 20 3 26 3 27 4)))

(expect (str "#------#\n"
             "#------#\n"
             "#------#\n"
             "#--S---#\n"
             "#--SJ--#\n"
             "########")
        (board->str test-board))

(expect [0 1 2 3 4 5 6 7 8 0 0] (str->row "#-IZSJLTOx--#"))

(expect (empty-board ["#ZZ----#"
                      "#-ZZ---#"
                      "#------#"
                      "#--S---#"
                      "#--SJ--#"])
  (set-piece test-board 2 0))

;; ########## Manual tests ##########

(defn write-board [board]
  (def filename "C:/Source/IDEA/tetrisanalyzer/clojure/target/tetris.txt")
  (def board-str (board->str board))
  (with-open [w (clojure.java.io/writer filename :append true)]
    (.write w (str "\n\n" board-str))))

;;(write-board test-board)
;;(write-board (set-piece test-board 2 0))
;;(write-board test-board3)


