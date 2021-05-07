(ns pipeline-transit.core-test
  (:require [clojure.test :refer :all]
            [pipeline-transit.core :as core]
            [time-literals.data-readers]
            [time-literals.read-write]))

(deftest write-transit-json-test
  (is (= "[\"~#'\",\"foo\"]" (core/write-transit-json "foo")))
  (is (= "[\"~#'\",\"~:foo\"]" (core/write-transit-json :foo)))
  (is (= "[\"~#joda-time\",\"2019-11-01T02:00:00.000Z\"]"
         (core/write-transit-json (org.joda.time.DateTime. 2019 11 1 2 0 (org.joda.time.DateTimeZone/UTC)))))

  (is (= "[\"~#time/date-time\",\"2019-11-01T02:00\"]"
         (core/write-transit-json #time/date-time "2019-11-01T02:00")))
  (is (= "[\"~#time/instant\",\"2019-11-01T02:00:00Z\"]"
         (core/write-transit-json #time/instant "2019-11-01T02:00:00Z"))))

(deftest read-transit-json-test
  (is (= :foo (core/read-transit-json "[\"~#'\",\"~:foo\"]"))))

(deftest transit-round-trip-test
  (are [coll] (= coll (core/read-transit-json (core/write-transit-json coll)))
     "testing"
     [:foo 3 "c"]
     :foo
     {:foo :bar}
     nil

     {:tick/date-time     #time/date-time "2019-11-01T02:00"
      :tick/time          #time/time "08:12:13.366"
      :clj-time/date-time (org.joda.time.DateTime. 2021 11 1 2 0 (org.joda.time.DateTimeZone/UTC))
      :uuid               (java.util.UUID/fromString "b5b5cabc-7bfe-4c33-a6a2-f0671eb913c8")
      :nil                nil}))

