# pipeline-transit

[![Clojars Project](https://img.shields.io/clojars/v/ai.motiva/pipeline-transit.svg)](https://clojars.org/ai.motiva/pipeline-transit)

This is a convenience library containing Transit handlers. It is used by other Motiva pipeline libraries.

## Usage

```clj
pipeline-transit.core=> (write-transit-str :foo)
"[\"~#'\",\"~:foo\"]"

pipeline-transit.core=> (read-transit-str *1)
:foo
```

Supports `java.time` objects.

```clj
pipeline-transit.core=> (require 'tick.alpha.api)
nil

pipeline-transit.core=> (write-transit-str (tick.alpha.api/date-time "2019-11-01T02:00"))
"[\"~#date-time\",\"2019-11-01T02:00\"]"

pipeline-transit.core=> (read-transit-str *1)
#time/date-time "2019-11-01T02:00"
```

