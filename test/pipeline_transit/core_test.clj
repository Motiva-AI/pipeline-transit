(ns pipeline-transit.core-test
  (:require [clojure.test :refer :all]
            [pipeline-transit.core :as core]

            [clj-time.core :as clj-time]
            [tick.alpha.api :as tick]))

(deftest write-transit-str-test
  (is (= "[\"~#'\",\"foo\"]" (core/write-transit-str "foo")))
  (is (= "[\"~#'\",\"~:foo\"]" (core/write-transit-str :foo)))
  (is (= "[\"~#joda-time\",\"2019-11-01T02:00:00.000Z\"]"
         (core/write-transit-str (clj-time/date-time 2019 11 1 2 0))))
  (is (= "[\"~#date-time\",\"2019-11-01T02:00\"]"
         (core/write-transit-str (tick/date-time "2019-11-01T02:00")))))

(deftest read-transit-str-test
  (is (= :foo (core/read-transit-str "[\"~#'\",\"~:foo\"]"))))

(deftest transit-round-trip-test
  (are [coll] (= coll (core/read-transit-str (core/write-transit-str coll)))
     "testing"
     [:foo 3 "c"]
     :foo
     {:foo :bar}

     {:tick/date-time     (tick/date-time "2019-11-01T02:00")
      :tick/time          (tick/time "19:03")
      :clj-time/date-time (clj-time/date-time 2019 11 1 2 0)
      :uuid               (java.util.UUID/fromString "b5b5cabc-7bfe-4c33-a6a2-f0671eb913c8")}))

