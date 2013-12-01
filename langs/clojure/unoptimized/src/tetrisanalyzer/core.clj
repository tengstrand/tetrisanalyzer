(ns tetrisanalyzer.core)

;; ===== Piece =====

(def pieces [ nil
  ;; I (1)
  [[[0 0] [1 0] [2 0] [3 0]]
   [[0 0] [0 1] [0 2] [0 3]]]

  ;; Z (2)
  [[[0 0] [1 0] [1 1] [2 1]]
   [[1 0] [0 1] [1 1] [0 2]]]

  ;; S (3)
  [[[1 0] [2 0] [0 1] [1 1]]
   [[0 0] [0 1] [1 1] [1 2]]]

  ;; J (4)
  [[[0 0] [1 0] [2 0] [2 1]]
   [[0 0] [1 0] [0 1] [0 2]]
   [[0 0] [0 1] [1 1] [2 1]]
   [[1 0] [1 1] [0 2] [1 2]]]

  ;; L (5)
  [[[0 0] [1 0] [2 0] [0 1]]
   [[0 0] [0 1] [0 2] [1 2]]
   [[2 0] [0 1] [1 1] [2 1]]
   [[0 0] [1 0] [1 1] [1 2]]]

  ;; T (6)
  [[[0 0] [1 0] [2 0] [1 1]]
   [[0 0] [0 1] [1 1] [0 2]]
   [[1 0] [0 1] [1 1] [2 1]]
   [[1 0] [0 1] [1 1] [1 2]]]

  ;; O (7)
  [[[0 0] [1 0] [0 1] [1 1]]]])

(def char->piece { \- 0 \I 1 \Z 2 \S 3 \J 4 \L 5 \T 6 \O 7 \x 8 \# 9 })

(defn piece->char [piece] (nth "-IZSJLTOx#" piece))

;; ===== Board =====

(defn- row->str [row]
  (apply str (map (fn [[_ piece]] (piece->char piece)) row)))

(defn board->str [board width]
  (->> board
       sort
       (partition width)
       (map row->str)
       (clojure.string/join "\n")))

(defn- str->row [row y]
  (map-indexed #(vector [y %1] (char->piece %2)) row))

(defn new-board
  ([] (new-board 12 21))
  ([rows] (into {} (mapcat #(str->row %1 %2) rows (range))))
  ([width height]
    (into {} (for [y (range height) x (range width)
                   :let [wall? (or (zero? x) (= x (dec width)) (= y (dec height)))]]
                   [[y x] (if wall? 9 0)]))))

;; ===== Move =====

(defn set-piece [board piece piece-shape]
  (apply assoc board (interleave piece-shape (repeat piece))))

(defn rotate-and-move-piece [piece rotation x y]
  (map (fn [[px py]] [(+ y py) (+ x px)]) ((pieces piece) rotation)))

(defn piece-occupied? [board piece {:keys [rotation x y]}]
  (not-every? zero? (map board (rotate-and-move-piece piece rotation x y))))

(defn- left [move bit-mask] (assoc move :x (dec (move :x))))
(defn- right [move bit-mask] (assoc move :x (inc (move :x))))
(defn- down [move bit-mask] (assoc move :y (inc (move :y))))
(defn- rotate [move bit-mask] (assoc move :rotation (bit-and (inc (move :rotation)) bit-mask)))

(defn valid-moves
  ([board piece bit-mask move visited valid-move]
    (if (contains? visited move) nil
      (if (piece-occupied? board piece move) valid-move
          (into #{} (mapcat (fn [[action valid-move]] (valid-moves board piece bit-mask (action move bit-mask) (conj visited move) valid-move))
                            [[left #{}] [right #{}] [rotate #{}] [down #{move}]]))))))
