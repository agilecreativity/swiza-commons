(ns b12n.swiza.commons.core-utils-test
  (:require [clojure.test :refer :all]
            [b12n.swiza.commons.core-utils :refer :all]))

(deftest core-utils-main-test
  (testing "assoc-some"
    (is (= (assoc-some {:a 1} :b 2)        {:a 1 :b 2}))
    (is (= (assoc-some {:a 1} :b 2 :c nil) {:a 1 :b 2}))
    (is (= (assoc-some {:a 1} :b nil)      {:a 1}))
    (is (= (assoc-some {:a 1} :b nil :c 3) {:a 1 :c 3})))

  (testing "camelize-keys"
    (is (= (camelize-keys {:abc 1 "java-example" 2}) {"abc" 1, "javaExample" 2}))
    (is (= (camelize-keys {:abc 1 "Java-example" 2}) {"abc" 1, "javaExample" 2})))

  (testing "kebabize-keys"
    (is (= (kebabtize-keys {:abc-def 1 "java-example" 2}) {:abc-def 1, :java-example 2}))
    (is (= (kebabtize-keys {:abc-DEF 1 "Java-example" 2}) {:abc-DEF 1, :java-example 2}))
    (is (= (kebabtize-keys {:abc-DEF 1 "JavaExample" 2}) {:abc-DEF 1, :java-example 2}))))
