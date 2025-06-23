(defproject tic-tac-toe "0.1.0-SNAPSHOT"
  :description "A terminal implemenation of the common game tic-tac-toe."
  :url "https://github.com/garrett-s-wininger/terminal-games/tic-tac-toe"
  :license {:name "The MIT License"
            :url "https://opensource.org/license/mit"}
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :main ^:skip-aot tic-tac-toe.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
