(ns home-ht-challenge.db
  (:require [defcomponent :refer [defcomponent]])
  (:import (com.zaxxer.hikari HikariDataSource HikariConfig)))

(defn- make-config [{:keys [host port name user password]}]
  (let [cfg (HikariConfig.)
        uri (str "jdbc:postgresql://" host ":" port "/" name)]
    (when uri (.setJdbcUrl cfg uri))
    (when user (.setUsername cfg user))
    (when password (.setPassword cfg password))
    cfg))

(defcomponent db
  []
  [config]
  (start [this]
         (->> (HikariDataSource. (make-config (:db config)))
              (assoc this :datasource)))
  (stop [this]
        (.close (:datasource pool))
        this))