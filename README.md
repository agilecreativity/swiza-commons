## swiza-commons

[![Clojars Project](https://img.shields.io/clojars/v/net.b12n/swiza-commons.svg)](https://clojars.org/net.b12n/swiza-commons)
[![Dependencies Status](https://jarkeeper.com/agilecreativity/swiza-commons/status.png)](https://jarkeeper.com/agilecreativity/swiza-commons)

Common Clojure library that I use for different projects and hopefully something that compatible with GraalVM.

### Basic Usage

If you are using Leiningen then you can quickly try

```shell
# Get project dependency
lein deps :tree
```

There are couple of function that I used quite often in other project.

```clojure
;; Use in your Clojure code
(require [b12n.swiza.commons.core-utils 
           :refer [expand-path 
                   load-edn-config 
                   assoc-some 
                   map-keys 
                   map-vals]]
         [b12n.swiza.common.base64-utils 
           :refer [base64-encode 
                   base64-decode]])
```

### Notes

- function that I used all the time would be:

```clojure
(expand-path "~/path") ;;=> "$HOME/path"
(load-edn-config "~/path/to/your/config.edn")

;; The following function works without issue when using with GraalVM
;; base64-encode 
;; base64-decode
```
