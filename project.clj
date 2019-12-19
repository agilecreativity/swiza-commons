(defproject net.b12n/swiza-commons "0.1.3"
  :description "Commonly used Clojure library"
  :url "http://github.com/agilecreativity/swiza-commons"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljfmt "0.6.1"]
            [jonase/eastwood "0.3.5"]
            [lein-auto "0.1.3"]
            [lein-cloverage "1.0.13"]
            [alembic "0.3.2"]]
  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"]
  :test-paths ["src/test/clojure"
               "src/test/java"]
  :dependencies [[aero "1.1.3"]
                 [clj-commons/fs "1.5.1"]
                 [camel-snake-kebab "0.4.0"]
                 [http-kit "2.3.0"]
                 [metosin/jsonista "0.2.2"]
                 [org.clojars.hozumi/clj-commons-exec "1.2.0"]
                 [org.clojure/clojure "1.9.0"]]
  :profiles {:dev {:global-vars {*warn-on-reflection* true
                                 *assert* true}}
             :1.9    {:dependencies [[org.clojure/clojure "1.9.0"]]}
             :1.10.1 {:dependencies [[org.clojure/clojure "1.10.1"]]}}
  :aliases {"lint" ["do" ["cljfmt" "check"] ["eastwood"]]
            "test-all" ["with-profile" "default:+1.9:+1.10" "test"]
            "lint-and-test-all" ["do" ["lint"] ["test-all"]]
            "test"   ["run" "-m" "circleci.test/dir" :project/test-paths]
            "tests"  ["run" "-m" "circleci.test"]
            "retest" ["run" "-m" "circleci.test.retest"]})
