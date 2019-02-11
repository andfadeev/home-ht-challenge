(ns home-ht-challenge.payments
  (:require [defcomponent :refer [defcomponent]]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as honey]
            [honeysql.helpers]
            [honeysql-postgres.format])
  (:import (java.time LocalDateTime)
           (java.sql Timestamp)))

(defn query
  [db q]
  (->> q
       (honey/format)
       (jdbc/query db)))

(defn list-payments
  [db contract-id start-date end-date]
  (->> {:select [:*]
        :from [:payments]
        :where [:and
                [:= :contract-id contract-id]
                [:>= :time (Timestamp/from start-date)]
                [:<= :time (Timestamp/from end-date)]
                [:= :is_deleted false]]}
       (query db)))

(defn time->sql
  [fields]
  (if (contains? fields :time)
    (update fields :time #(Timestamp/from %))
    fields))

(defn create-payment
  [db payment]
  (first (jdbc/insert! db :payments (time->sql payment))))

(defn update-payment
  [db payment-id fields]
  (->> {:update :payments
        :set (-> (time->sql fields)
                 (assoc :updated_at (LocalDateTime/now)))
        :where [:= :id payment-id]
        :returning [:*]}
       (query db)
       (first)))

(defn delete-payment
  [db payment-id]
  (->> {:update :payments
        :set {:is_deleted true}
        :where [:= :id payment-id]
        :returning [:*]}
       (query db)
       (first)))