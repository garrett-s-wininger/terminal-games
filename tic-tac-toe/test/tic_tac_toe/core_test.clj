(ns tic-tac-toe.core-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe.core :refer :all]))

(def empty-game-state ["1" "2" "3" "4" "5" "6" "7" "8" "9"])

(deftest are-diagonals-victorious?-test
  (testing "Should properly identify non-winning diagonals"
    (is (not (are-diagonals-victorious? empty-game-state))
    (is (not (are-diagonals-victorious? ["O" "" "" "" "X" "" "" "" "X"])))))
  (testing "Should properly identify winning diagonals"
    (is (are-diagonals-victorious? ["X" "" "" "" "X" "" "" "" "X"]))
    (is (are-diagonals-victorious? ["" "" "O" "" "O" "" "O" "" ""]))))

(deftest is-column-victorious?-test
  (testing "Should properly identify a non-matching column"
    (is (not (is-column-victorious? empty-game-state 2)))
    (is (not (is-column-victorious? ["" "O" "" "" "X" "" "" "X" ""] 1))))
  (testing "Should properly identify a maching column"
    (is (is-column-victorious? ["X" "" "" "X" "" "" "X" "" ""] 0))))

(deftest is-position-taken?-test
  (testing "Should properly identify an available position"
    (is (not (is-position-taken? empty-game-state 1))))
  (testing "Should properly identify a taken position"
    (is (is-position-taken? ["X" "" "" "" "" "" "" "" ""] 0))
    (is (is-position-taken? ["" "" "" "" "" "" "" "" "O"] 8))))

(deftest is-row-victorious?-test
  (testing "Should properly identify a non-matching row"
    (is (not (is-row-victorious? empty-game-state 0)))
    (is (not (is-row-victorious? ["X" "O" "X" "" "" "" "" "" ""] 0))))
  (testing "Should properly identify a matching row"
    (is (is-row-victorious? ["X" "X" "X" "" "" "" "" "" ""] 0))
    (is (is-row-victorious? ["" "" "" "O" "O" "O" "" "" ""] 1))))
