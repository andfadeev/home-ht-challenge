(ns home-ht-challenge.schema
  (:require [schema.core :as s])
  (:import (java.util Date)))

(def payment
  {:id s/Int
   :contractId s/Int
   :description s/Str
   :value s/Int
   :time Date
   :createdAt Date
   :updatedAt (s/maybe Date)
   :isDeleted s/Bool
   :isImported s/Bool})