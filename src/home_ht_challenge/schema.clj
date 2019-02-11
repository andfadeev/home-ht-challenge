(ns home-ht-challenge.schema
  (:require [schema.core :as s])
  (:import (java.util Date)
           (java.time Instant)))

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

(def create-payment-body
  {:description s/Str
   :time Instant
   :value s/Int})

(def update-payment-body
  (s/conditional
    (complement empty?)
    {(s/optional-key :description) s/Str
     (s/optional-key :time) Instant
     (s/optional-key :value) s/Int}))

(def list-payments-query
  {:startDate Instant
   :endDate Instant})