(ns tetrisanalyzer.piece.bitmask)

;; The mapping between number of shapes/rotations and the rotation bitmask
(def num-shapes->bitmask {1 0   ;; 00 = always 0
                          2 1   ;; 01 = cycles between 0 and 1
                          4 3}) ;; 11 = cycles between 0, 1, 2, and 3

(defn rotation-bitmask [shapes p]
  (-> p shapes count num-shapes->bitmask))
