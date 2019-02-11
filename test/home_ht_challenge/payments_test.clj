(ns home-ht-challenge.payments-test
  (:require [clojure.test :refer :all]
            [home-ht-challenge.payments :as payments])
  (:import (java.time Instant)
           (java.sql Timestamp)))

(deftest time->sql-test
  (testing "If key :time exists coerce to java.sql.Timestamp"
    (let [time (Instant/now)
          timestamp (Timestamp/from time)
          fields {:description "Some description"}]
      (is (= (assoc fields :time timestamp)
             (payments/time->sql (assoc fields :time time))))))

  (testing "If no key :time exists return"
    (let [fields {:description "Some description"}]
      (is (= fields (payments/time->sql fields))))))