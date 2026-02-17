(ns tetrisanalyzer.piece.shape)

(defn cell [x character y]
  (when (= \x character)
    [x y]))

(defn row-cells [y row]
  (keep-indexed #(cell %1 %2 y)
                (str row)))

(defn shape [piece-grid]
  (vec (mapcat identity
               (map-indexed row-cells piece-grid))))

(defn shapes [piece-grids]
  (mapv #(mapv shape %)
        piece-grids))
