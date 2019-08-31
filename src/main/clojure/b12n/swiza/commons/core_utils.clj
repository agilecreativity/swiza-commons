(ns b12n.swiza.commons.core-utils
  (:require
   [me.raynes.fs :as fs :refer [expand-home normalized]]
   [aero.core :refer [read-config]]
   [clojure.java.io :as io]
   [jsonista.core :refer [read-value write-value object-mapper]]))

(defn expand-path
  [filename]
  (-> filename
      fs/expand-home
      fs/normalized
      str))

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

(defn ^:private token-for-env
  "Token for a given env.

  (token-for-env base-config :test)"
  [config env]
  (-> (config-for-env config env)
      :options
      :headers
      :authorization))

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
