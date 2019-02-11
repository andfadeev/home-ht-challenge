(ns home-ht-challenge.server
  (:require [ring.adapter.jetty :as jetty]
            [muuntaja.middleware]
            [defcomponent :refer [defcomponent]]
            [schema.core :as s]
            [muuntaja.core :as m]
            [ring.middleware.params :as params]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.coercion :as coercion]
            [reitit.coercion.schema :as coercion-schema]
            [reitit.ring :as ring]
            [home-ht-challenge
             [payments :as payments]
             [db :as db]
             [schema :as schema]]
            [camel-snake-kebab.core :refer :all]
            [camel-snake-kebab.extras :refer [transform-keys]])
  (:import (java.time Instant)))

(defn- response-format
  [response-body]
  (transform-keys ->camelCaseKeyword response-body))

(defn- ok
  [body]
  {:status 200
   :body (response-format body)})

(defn- get-payment-id
  [req]
  (get-in req [:parameters :path :paymentId]))

(defn- get-contract-id
  [req]
  (get-in req [:parameters :path :contractId]))

(defn sum-payments
  [payments]
  (apply + (keep :value payments)))

(defn list-payments
  [{:keys [db]} req]
  (let [contractId (get-contract-id req)
        {:keys [startDate endDate]} (get-in req [:parameters :query])]
    (let [payments (payments/list-payments db contractId startDate endDate)]
      (ok {:sum (sum-payments payments)
           :items payments}))))

(defn add-payment
  [{:keys [db]} req]
  (let [contractId (get-contract-id req)
        payment (-> (get-in req [:parameters :body])
                    (assoc :contract_id contractId))]
    (ok (payments/add-payment db payment))))

(defn update-payment
  [{:keys [db]} req]
  (let [paymentId (get-payment-id req)
        fields (get-in req [:parameters :body])]
    (ok (payments/update-payment db paymentId fields))))

(defn delete-payment
  [{:keys [db]} req]
  (let [paymentId (get-in req [:parameters :path :paymentId])]
    (ok (payments/delete-payment db paymentId))))

(defn routes
  [this]
  [["/contract/:contractId"
    {:coercion coercion-schema/coercion
     :parameters {:path {:contractId s/Int}}}
    ["/payments"
     {:responses {200 {:body {:sum s/Int
                              :items [schema/payment]}}}
      :get {:summary "List payments for contractId and startDate <= time <= endDate"
            :parameters {:query {:startDate Instant
                                 :endDate Instant}}
            :handler (partial list-payments this)}}]
    ["/payment"
     {:responses {200 {:body schema/payment}}
      :post {:summary "Create new payment"
             :parameters {:body {:description s/Str
                                 :time Instant
                                 :value s/Int}}
             :handler (partial add-payment this)}}]]
   ["/payment/:paymentId"
    {:coercion coercion-schema/coercion
     :parameters {:path {:paymentId s/Int}}}
    ["/delete"
     {:responses {200 {:body schema/payment}}
      :post {:summary "Mark payment as deleted"
             :handler (partial delete-payment this)}}]
    ["/update"
     {:responses {200 {:body schema/payment}}
      :post {:summary "Update payment"
             :parameters {:body
                          (s/conditional
                            (complement empty?)
                            {(s/optional-key :description) s/Str
                             (s/optional-key :time) Instant
                             (s/optional-key :value) s/Int})}
             :handler (partial update-payment this)}}]]])

(defcomponent http-server
  [db/db]
  [config]
  (start
    [this]
    (let [opts
          {:data {:muuntaja m/instance
                  :middleware [params/wrap-params
                               muuntaja/format-middleware
                               coercion/coerce-exceptions-middleware
                               coercion/coerce-request-middleware
                               coercion/coerce-response-middleware]}}
          server
          (-> (ring/ring-handler
                (ring/router [(routes this)] opts)
                (ring/create-default-handler))
              (jetty/run-jetty {:port 3000
                                :join? false}))]
      (assoc this :server server)))
  (stop
    [this]
    (.stop (:server this))
    this))
