
(ns app.services.backend.handler.helpers
    (:require [candy.api :refer [return]]
              [mixed.api :as mixed]
              [mongo-db.api     :as mongo-db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-service-price
  ; @param (namespaced map) service-link
  ;  {:service/count (integer)
  ;   :service/id (string)}
  ;
  ; @return (integer)
  [{:service/keys [count id]}]
  (if-let [{:service/keys [price]} (mongo-db/get-document-by-id "services" id)]
          (mixed/multiply-numbers count price)
          (return 0)))

(defn get-services-price
  ; @param (namespaced maps in vector) services
  ;  [{:service/count (integer)
  ;    :service/id (string)}]
  ;
  ; @return (integer)
  [services]
  (letfn [(f [result service] (+ result (get-service-price service)))]
         (reduce f 0 services)))
