(defproject ai.motiva/pipeline-transit "0.1.1"
  :description "Transit handlers for other Motiva pipeline libraries"
  :license {:name "MIT License"
            :url  "https://opensource.org/licenses/MIT"}

  :repl-options {:init-ns pipeline-transit.core}

  :dependencies [[com.cognitect/transit-clj "1.0.329"]
                 [joda-time/joda-time "2.12.0"]
                 [time-literals "0.1.5"]]

  :profiles {:dev {:source-paths ["src" "dev/src"]
                   ;; "test" is included by default - adding it here confuses
                   ;; circleci.test which runs everything twice.
                   :test-paths []
                   :resource-paths ["dev/resources"]

                   :dependencies [[org.clojure/clojure "1.11.1"]
                                  [circleci/circleci.test "0.5.0"]]}}

  :repositories [["releases" {:url           "https://clojars.org/repo"
                              :username      "motiva-ai"
                              :password      :env
                              :sign-releases false}]])

