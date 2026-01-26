(ns tetrisanalyzer.piece.settings.nintendo-nes
  (:require [tetrisanalyzer.piece.shape :as shape]))

;; https://strategywiki.org/wiki/Tetris/Rotation_systems#Nintendo_rotation_system

;; start-x = 4
;; kick? = false

(def O0 ['----
         '-xx-
         '-xx-
         '----])

(def I0 ['----
         '----
         'xxxx
         '----])

(def I1 ['--x-
         '--x-
         '--x-
         '--x-])

(def Z0 ['---
         'xx-
         '-xx])

(def Z1 ['--x
         '-xx
         '-x-])

(def S0 ['---
         '-xx
         'xx-])

(def S1 ['-x-
         '-xx
         '--x])

(def J0 ['---
         'xxx
         '--x])

(def J1 ['-xx
         '-x-
         '-x-])

(def J2 ['x--
         'xxx
         '---])

(def J3 ['-x-
         '-x-
         'xx-])

(def L0 ['---
         'xxx
         'x--])

(def L1 ['-x-
         '-x-
         '-xx])

(def L2 ['--x
         'xxx
         '---])

(def L3 ['xx-
         '-x-
         '-x-])

(def T0 ['---
         'xxx
         '-x-])

(def T1 ['-x-
         '-xx
         '-x-])

(def T2 ['-x-
         'xxx
         '---])

(def T3 ['-x-
         'xx-
         '-x-])

(def pieces [[O0]
             [I0 I1]
             [Z0 Z1]
             [S0 S1]
             [J0 J1 J2 J3]
             [L0 L1 L2 L3]
             [T0 T1 T2 T3]])

(def shapes (shape/shapes pieces))
