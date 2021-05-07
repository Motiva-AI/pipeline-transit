(ns pipeline-transit.core
  (:require [cognitect.transit :as t]
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
  (merge
    (into {}
          (for [[tick-class host-class] time-classes]
            [host-class (t/write-handler (constantly (name tick-class)) str)]))

    {org.joda.time.DateTime (t/write-handler "joda-time" str)}))

(def ^:private read-handlers
  (merge
    (into {}
          (for [[sym fun] time-literals.read-write/tags]
            [(name sym) (t/read-handler fun)])) ; omit "time/" for brevity

    {"joda-time" (t/read-handler (fn [r] (org.joda.time.DateTime/parse r)))}))

(defn write-transit [coll output-stream]
  (t/write (t/writer output-stream :json {:handlers write-handlers}) coll))

(defn write-transit-bytes ^bytes [o]
  (let [os (ByteArrayOutputStream.)]
    (write-transit o os)
    (.toByteArray os)))

(defn write-transit-str ^String [o]
  (String. (write-transit-bytes o) "UTF-8"))

(defn read-transit [input-stream]
  (t/read (t/reader input-stream :json {:handlers read-handlers})))

(defn read-transit-str [^String s]
  (read-transit (ByteArrayInputStream. (.getBytes s "UTF-8"))))

