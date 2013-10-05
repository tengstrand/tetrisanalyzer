(ns tetrisanalyzer.position-test
  (:require [expectations :refer :all]
            [tetrisanalyzer.position :refer :all]
            [tetrisanalyzer.board :refer :all]
            [tetrisanalyzer.piece :refer :all]))

;; Should create a vector used to place a piece (7) on a board with width 4.
(expect [0 7 1 7 2 7 5 7] (dots->piece [[0 0][1 0][2 0][1 1]] 4 7))

;; Should create a vector used to place the the different shapes of piece Z on a board with width 4.
(expect [[0 2 1 2 5 2 6 2][1 2 4 2 5 2 8 2]]
        (piece->rotations Z 4))

(expect {:board {:width 6,
                 :height 5,
                 :dots [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]},
         :pieces [[]
                  [[0 1 1 1 2 1 3 1] [0 1 6 1 12 1 18 1]]
                  [[0 2 1 2 7 2 8 2] [1 2 6 2 7 2 12 2]]
                  [[1 3 2 3 6 3 7 3] [0 3 6 3 7 3 13 3]]
                  [[0 4 1 4 2 4 8 4] [0 4 1 4 6 4 12 4] [0 4 6 4 7 4 8 4] [1 4 7 4 12 4 13 4]]
                  [[0 5 1 5 2 5 6 5] [0 5 6 5 12 5 13 5] [2 5 6 5 7 5 8 5] [0 5 1 5 7 5 13 5]]
                  [[0 6 1 6 2 6 7 6] [0 6 6 6 7 6 12 6] [1 6 6 6 7 6 8 6] [1 6 6 6 7 6 13 6]]
                  [[0 7 1 7 6 7 7 7]] []]}
    (new-position (new-board)))

(def expected-position
  {:board {:width 6,
           :height 5,
           :dots [0 2 0 0 0 0 2 2 0 0 0 0 2 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]}
   :pieces [[]
            [[0 1 1 1 2 1 3 1] [0 1 6 1 12 1 18 1]]
            [[0 2 1 2 7 2 8 2] [1 2 6 2 7 2 12 2]]
            [[1 3 2 3 6 3 7 3] [0 3 6 3 7 3 13 3]]
            [[0 4 1 4 2 4 8 4] [0 4 1 4 6 4 12 4] [0 4 6 4 7 4 8 4] [1 4 7 4 12 4 13 4]]
            [[0 5 1 5 2 5 6 5] [0 5 6 5 12 5 13 5] [2 5 6 5 7 5 8 5] [0 5 1 5 7 5 13 5]]
            [[0 6 1 6 2 6 7 6] [0 6 6 6 7 6 12 6] [1 6 6 6 7 6 8 6] [1 6 6 6 7 6 13 6]]
            [[0 7 1 7 6 7 7 7]]
            []]}
  )

(defn test-position []
  (def board2 (tetrisanalyzer.board/new-board))
  (def position (new-position board2))
   position)

(def tp (test-position))

(def position-result (set-piece tp 2 1))

(expect expected-position position-result)

;; ---------- Manual test ----------

;;(def content (str (set-piece position 2 1)))

;;(tetrisanalyzer.file/write-file! "C:/Source/IDEA/tetrisanalyzer/clojure/target/tetris.txt" content)

