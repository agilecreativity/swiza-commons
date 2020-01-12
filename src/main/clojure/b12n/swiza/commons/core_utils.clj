(ns b12n.swiza.commons.core-utils
  (:require
   [aero.core :refer [read-config]]
   [camel-snake-kebab.core :as csk]
   [clojure.java.io :as io]
   [clojure.walk :refer [postwalk]]
   [jsonista.core :refer [read-value write-value object-mapper]]
   [me.raynes.fs :as fs :refer [expand-home normalized]]))

(defn expand-path
  [filename]
  (-> filename
      fs/expand-home
      fs/normalized
      str))

(comment

  (expand-path "~/Calibre Library")
  ;;=> "/Users/bchoomnuan/Calibre Library"

  (expand-home "~/Calibre Library")
  ;;=> #object[java.io.File 0x3fe7c8f6 "/Users/bchoomnuan/Calibre Library"]

  (let [dir
        #_"~/Calibre Library"
        "\"~/Calibre Library\""]
    (if-let [_ (re-find #"\"" dir)]
      (format "\"%s\"" (expand-home (clojure.string/replace dir "\"" "")))
      (expand-home dir)))

  (normalized (expand-home "\"~/Calibre Library\"")))

(defn load-edn-config
  "Load the edn config from a given file."
  [config]
  (read-config (expand-path config)))

(defn remove-nil
  "Remove nil value from a given map"
  [input-map]
  (reduce (fn [m [k v]]
            (if (nil? v)
              m
              (assoc m k v)))
          {}
          input-map))

;; TODO: move this to specific project
(defn config-for-env
  "Get a base config for a given env.
  Example:
  (config-for-env \"./config.edn\" :test)
  or
  (config-for-env \"./config.edn\" \"test\")"
  [config env]
  (let [{:keys [url token]}
        (select-keys ((load-edn-config config) (keyword env))
                     [:url :token])]
    {:url url
     :options {:headers {:authorization token}
               :accept :json, :content-type :json}}))

;; TODO: move this to specific project
(defn ^:private token-for-env
  "Token for a given env.

  (token-for-env base-config :test)"
  [config env]
  (-> (config-for-env config env)
      :options
      :headers
      :authorization))

;; TODO: move this to specific project
(defn default-options
  [config env & [opts]]
  (let [default-opts {:timeout 1000 ;; ms
                      :oauth-token (token-for-env config env)}]
    (if opts
      (merge default-opts opts)
      default-opts)))

(defn assoc-some
  ;; From weavejester/medley
  "Associates a key with a value in a map, if and only if the value is not nil."
  ([m k v]
   (if (nil? v) m (assoc m k v)))
  ([m k v & kvs]
   (reduce (fn [m [k v]] (assoc-some m k v))
           (assoc-some m k v)
           (partition 2 kvs))))

(defn write-json
  "Write data as JSON to file"
  [file data]
  (write-value (io/file file) data (object-mapper {:pretty true})))

(defn read-json
  "Read data as JSON from string or file"
  [file]
  (read-value file (object-mapper {:decode-key-fn true})))

(defn map-keys
  "Given a function and a map, returns the map resulting from applying
  the function to each key.

  e.g. (map-keys name {:a 1 :b 2 :c 3}) ;;=> {\"a\" 1, \"b\" 2, \"c\" 3}
  "
  [f m]
  (zipmap (map f (keys m)) (vals m)))

(defn map-vals
  "Given a function and a map, returns the map resulting from applying
  the function to each value.

  e.g. (map-vals inc {:a 1 :b 2 :c 3}) ;;=> {:a 2, :b 3, :c 4}
  "
  [f m]
  (zipmap (keys m) (map f (vals m))))

(defn kebabtize-keys
  "Recursively transforms all map keys from strings to kabab-case-keyword."
  [m]
  (let [f (fn [[k v]]
            (if (string? k)
              [(csk/->kebab-case-keyword k) v] [k v]))]
    (postwalk (fn [x]
                (if (map? x)
                  (into {} (map f x)) x)) m)))

(comment
  (kebabtize-keys {"firstItem" "first"
                   "secondItem" "2nd"
                   "third-nested-items" {"InnerItem" "items"}})
  ;;=> {:first-item "first", :second-item "2nd", :third-nested-items {:inner-item "items"}}
)

(defn camelize-keys
  "Recursively transforms all map keys from keywords, symbol or string to camelCaseString."
  [m]
  (let [f (fn [[k v]]
            (if (or (keyword? k)
                    (symbol? k)
                    (string? k))
              [(-> k
                   csk/->kebab-case-keyword
                   csk/->camelCaseString) v]
              [k v]))]
    (postwalk (fn [x]
                (if (map? x)
                  (into {} (map f x)) x)) m)))

(comment
  (camelize-keys {:this-is-a-test "first"
                  :second-item "2nd"})

  ;;=> {"thisIsATest" "first", "secondItem" "2nd"}
)
