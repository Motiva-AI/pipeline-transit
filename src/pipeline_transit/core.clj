(ns pipeline-transit.core
  (:require [cognitect.transit :as transit]
            [time-literals.read-write])
  (:import [java.io ByteArrayOutputStream ByteArrayInputStream]
           [java.time
            Period
            LocalDate
            LocalDateTime
            ZonedDateTime
            OffsetTime
            Instant
            OffsetDateTime
            ZoneId
            DayOfWeek
            LocalTime
            Month
            Duration
            Year
            YearMonth]))

(def ^:private time-classes
  {'period           Period
   'date             LocalDate
   'date-time        LocalDateTime
   'zoned-date-time  ZonedDateTime
   'offset-time      OffsetTime
   'instant          Instant
   'offset-date-time OffsetDateTime
   'time             LocalTime
   'duration         Duration
   'year             Year
   'year-month       YearMonth
   'zone             ZoneId
   'day-of-week      DayOfWeek
   'month            Month})

(def ^:private write-handlers
  {:handlers
   (-> (into {}
             (for [[tick-class host-class] time-classes]
               [host-class (transit/write-handler (constantly (name tick-class)) str)]))
       (assoc org.joda.time.DateTime
              (transit/write-handler "joda-time" (fn [v] (str v)))))})

(def ^:private read-handlers
  {:handlers
   (-> (into {} (for [[sym fun] time-literals.read-write/tags]
              [(name sym) (transit/read-handler fun)])) ; omit "time/" for brevity
       (assoc "joda-time"
              (transit/read-handler (fn [r] (org.joda.time.DateTime/parse r)))))})

(defn transit-write
  [x]
  (let [baos   (ByteArrayOutputStream.)
        writer (transit/writer baos :json write-handlers)
        _      (transit/write writer x)
        return (.toString baos)]
    (.reset baos)
    return))

(defn transit-read
  [json]
  (when json
    (-> (.getBytes json)
        (ByteArrayInputStream.)
        (transit/reader :json read-handlers)
        (transit/read))))

