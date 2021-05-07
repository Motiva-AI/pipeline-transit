(defproject ai.motiva/pipeline-transit "0.1.0-SNAPSHOT"
  :description "Transit handlers for other Motiva pipeline libraries"
  :license {:name "MIT License"
            :url  "https://opensource.org/licenses/MIT"}

  :repl-options {:init-ns pipeline-transit.core}

  :dependencies [[com.cognitect/transit-clj "1.0.324"]
                 [tick "0.4.26-alpha"]]

    :profiles {:dev {:source-paths ["src" "dev/src"]
                   ;; "test" is included by default - adding it here confuses
                   ;; circleci.test which runs everything twice.
                   :test-paths []
                   :resource-paths ["dev/resources"]

                   :dependencies [[org.clojure/clojure "1.10.3"]
                                  [circleci/circleci.test "0.5.0"]]}

             :provided {:dependencies [[clj-time "0.15.2"]]}})
