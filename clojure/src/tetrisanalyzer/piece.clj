(ns tetrisanalyzer.piece)

;; -
(def A {:index 0 :char \- })

;; I
(def I0 [[0 0][1 0][2 0][3 0]])
(def I1 [[0 0][0 1][0 2][0 3]])
(def I {:index 1 :char \I :widths [3 2 3 2] :heights [2 3 2 3] :rotations [I0 I1]})

;; Z
(def Z0 [[0 0][1 0][1 1][2 1]])
(def Z1 [[1 0][0 1][1 1][0 2]])
(def Z {:index 2 :char \Z :widths [3 2] :heighs [2 3] :rotations [Z0 Z1]})

;; S
(def S0 [[1 0][2 0][0 1][1 1]])
(def S1 [[0 0][0 1][1 1][1 2]])
(def S {:index 3 :char \S :widths [3 2] :heiths [2 3] :rotations [S0 S1]})

;; J
(def J0 [[0 0][1 0][2 0][2 1]])
(def J1 [[0 0][1 0][0 1][0 2]])
(def J2 [[0 0][0 1][1 1][2 1]])
(def J3 [[1 0][1 1][0 2][1 2]])
(def J {:index 4, :char \J :widths [3 2 3 2] :heights [2 3 2 3] :rotations [J0 J1 J2 J3]})

;; L
(def L0 [[0 0][1 0][2 0][0 1]])
(def L1 [[0 0][0 1][0 2][1 2]])
(def L2 [[2 0][0 1][1 1][2 1]])
(def L3 [[0 0][1 0][1 1][1 2]])
(def L {:index 5, :char \L :widths [3 2 3 2] :heights [2 3 2 3] :rotations [L0 L1 L2 L3]})

;; T
(def T0 [[0 0][1 0][2 0][1 1]])
(def T1 [[0 0][0 1][1 1][0 2]])
(def T2 [[1 0][0 1][1 1][2 1]])
(def T3 [[1 0][0 1][1 1][1 2]])
(def T {:index 6, :char \T :widths [3 2 3 2] :heights [2 3 2 3] :rotations [T0 T1 T2 T3]})

;; O
(def O0 [[0 0][1 0][0 1][1 1]])
(def O {:index 7 :char \O :widths [2] :heiths [2] :rotations [O0]})

;; X
(def X {:index 8 :char \x })

(def pieces [A I Z S J L T O X])

(def char->piece {\- 0 \I 1 \Z 2 \S 3 \J 4 \L 5 \T 6 \O 7 \x 8})

(defn piece->char [piece] (nth "-IZSJLTOx" piece))
