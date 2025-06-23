(ns tic-tac-toe.core
  (:gen-class))

(def game-states #{:in-progress :draw :victory})

(defn char-for-player
  "Obtains the visual character for the current player"
  [player-idx]
  {:pre [(int? player-idx)
         (> player-idx 0)
         (< player-idx 3)]
   :post [(string? %)]}
  (if (= player-idx 1)
    "X"
    "O"))

(defn is-full-board?
  "Determines whether the given board has moves remaining"
  [game-state]
  {:pre [(vector? game-state)]
   :post [(boolean? %)]}
  (every? (fn [position] (or (= position "X") (= position "O"))) game-state))

(defn is-position-taken?
  "Determines whether the given position has been taken by a player"
  [game-state position]
  {:pre [(vector? game-state)
         (= (.size game-state) 9)]
   :post [(boolean? %)]}
  (or (= (get-in game-state [position]) "X")
      (= (get-in game-state [position]) "O")))

(defn is-row-victorious?
  "Determines if a given row meets the win conditions"
  [game-state row-number]
  {:pre [(vector? game-state)
         (= (.size game-state) 9)
         (> row-number -1)
         (< row-number 3)]
   :post [(boolean? %)]}
  (let [row-start (* 3 row-number)]
    (and
      (=
        (get-in game-state [row-start])
        (get-in game-state [(+ row-start 1)])
        (get-in game-state [(+ row-start 2)]))
      (is-position-taken? game-state row-start)
      (is-position-taken? game-state (+ row-start 1))
      (is-position-taken? game-state (+ row-start 2)))))

(defn is-column-victorious?
  "Determines if a given column meets the win conditions"
  [game-state column-number]
  {:pre [(vector? game-state)
         (= (.size game-state) 9)
         (> column-number -1)
         (< column-number 3)]
   :post [(boolean? %)]}
  (and
    (=
      (get-in game-state [column-number])
      (get-in game-state [(+ column-number 3)])
      (get-in game-state [(+ column-number 6)]))
    (is-position-taken? game-state column-number)
    (is-position-taken? game-state (+ column-number 3))
    (is-position-taken? game-state (+ column-number 6))))

(defn are-diagonals-victorious?
  "Determines whether either of the possible diagonal win conditions are met"
  [game-state]
  {:pre [(vector? game-state)
         (= (.size game-state) 9)]
   :post [(boolean? %)]}
  (or
    (and
      (=
        (get-in game-state [2])
        (get-in game-state [4])
        (get-in game-state [6]))
      (is-position-taken? game-state 2)
      (is-position-taken? game-state 4)
      (is-position-taken? game-state 6))
    (and
      (=
        (get-in game-state [0])
        (get-in game-state [4])
        (get-in game-state [8]))
      (is-position-taken? game-state 0)
      (is-position-taken? game-state 4)
      (is-position-taken? game-state 8))))

(defn game-status
  "Identifies the current status of the game given its state"
  [game-state]
  {:pre [(vector? game-state)
         (= (.size game-state) 9)]
   :post [(contains? game-states %)]}
  (if
    (is-full-board? game-state)
    :draw
    (if
      (or
        (are-diagonals-victorious? game-state)
        (is-column-victorious? game-state 0)
        (is-column-victorious? game-state 1)
        (is-column-victorious? game-state 2)
        (is-row-victorious? game-state 0)
        (is-row-victorious? game-state 1)
        (is-row-victorious? game-state 2))
      :victory
      :in-progress)))

(defn print-row
  "Renders a given row of the board onto the terminal"
  [game-state row-index]
  {:pre [(vector? game-state)
         (= (.size game-state) 9)
         (int? row-index)
         (> row-index 0)
         (< row-index 4)]}
  (println
    (format " %s | %s | %s "
      (get-in game-state [(* 3 (- row-index 1))])
      (get-in game-state [(+ (* 3 (- row-index 1)) 1)])
      (get-in game-state [(+ (* 3 (- row-index 1)) 2)]))))

(defn print-row-separator
  "Renders a row separator onto the terminal"
  []
  (println "-----------"))

(defn print-board
  "Renders the board onto the terminal"
  [game-state]
  {:pre [(vector? game-state)
         (= (.size game-state) 9)]}
  (println)
  (print-row game-state 1)
  (print-row-separator)
  (print-row game-state 2)
  (print-row-separator)
  (print-row game-state 3)
  (println))

(defn prompt-for-move
  "Asks the player for their move selection"
  [player-index]
  {:pre [(int? player-index)
         (> player-index 0)
         (< player-index 3)]}
  (println
    (format "Player %d (%s), choose your move:"
      player-index
      (char-for-player player-index))))

(defn run-game
  "Main game loop for tic-tac-toe"
  [game-state player-index]
  {:pre [(vector? game-state)
         (= (.size game-state) 9)
         (int? player-index)
         (> player-index 0)
         (< player-index 3)]}
  (print-board game-state)
  (prompt-for-move player-index)
  (try
    (let
      [moveIdx (Integer/parseInt (read-line))]
      (if (or (< moveIdx 1) (> moveIdx 9))
        (do
          (println
            "[ERROR] Input must be in the range of 1 to 9 (inclusive), please try again.")
          (run-game game-state player-index))
        (if (is-position-taken? game-state (- moveIdx 1))
          (do
            (println
              "[ERROR] Position is already taken, please try again.")
            (run-game game-state player-index))
          (let
            [updated-state (assoc game-state (- moveIdx 1) (char-for-player player-index))]
            (case (game-status updated-state)
              :in-progress (run-game updated-state (if (= player-index 1) 2 1))
              :draw (do
                      (println)
                      (println "********")
                      (println "* DRAW *")
                      (println "********")
                      (print-board updated-state))
              :victory (do
                          (println)
                          (println "***********")
                          (println "* VICTORY *")
                          (println "***********")
                          (print-board updated-state)
                          (println
                            (format
                              "Player %d (%s) wins!\n"
                              player-index
                              (char-for-player player-index)))))))))
    (catch NumberFormatException e
      (println "[ERROR] Input must be a number, please try again.")
      (run-game game-state player-index))))

(defn -main
  "Runs a terminal-based clone of Tic-Tac-Toe"
  [& args]
  (println)
  (println "***************")
  (println "* Tic-Tac-Toe *")
  (println "***************")
  (let
    [initial-state [" " " " " " " " " " " " " " " " " "]]
    (run-game initial-state 1)))
