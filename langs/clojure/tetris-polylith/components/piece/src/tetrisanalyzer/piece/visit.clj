(ns tetrisanalyzer.piece.visit)

(defn visited? [visited-moves x y rotation]
  (if-let [visited-rotations (get-in visited-moves [y x])]
    (not (zero? (bit-and visited-rotations
                         (bit-shift-left 1 rotation))))
    true)) ;; Cells outside the board are treated as visited

(defn visit [visited-moves x y rotation]
  (assoc-in visited-moves [y x] (bit-or (get-in visited-moves [y x])
                                        (bit-shift-left 1 rotation))))
