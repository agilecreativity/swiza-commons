(defproject net.b12n/swiza-commons "0.1.5"
  :description "Commonly used Clojure library"
  :url "http://github.com/agilecreativity/swiza-commons"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljfmt "0.6.7"]
            [jonase/eastwood "0.3.11"]
            [lein-auto "0.1.3"]
            [lein-cloverage "1.1.2"]
            [alembic "0.3.2"]]
  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"]
  :test-paths ["src/test/clojure"
               "src/test/java"]
  :dependencies [[aero "1.1.6"]
                 [clj-commons/fs "1.5.2"]
                 [camel-snake-kebab "0.4.1"]
                 [metosin/jsonista "0.2.6"]
                 [org.clojure/clojure "1.10.2-alpha1"]]
  :profiles {:dev {:global-vars {*warn-on-reflection* true
                                 *assert* true}}
             :1.10 {:dependencies [[org.clojure/clojure "1.10.2-alpha1"]]}
             :1.9  {:dependencies [[org.clojure/clojure "1.9.0"]]}}
  :aliases {"test-all" ["with-profile" "default:+1.9:+1.10" "test"]})
