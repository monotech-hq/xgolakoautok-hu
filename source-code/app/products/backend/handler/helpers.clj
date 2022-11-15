
(ns app.products.backend.handler.helpers
    (:require [candy.api    :refer [return]]
              [mixed.api    :as mixed]
              [mongo-db.api :as mongo-db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-product-price
  ; @param (namespaced map) product-link
  ;  {:product/count (integer)
  ;   :product/id (string)}
  ;
  ; @return (integer)
  [{:product/keys [count id]}]
  (if-let [{:product/keys [unit-price]} (mongo-db/get-document-by-id "products" id)]
          (mixed/multiply-numbers count unit-price)
          (return 0)))

(defn get-products-price
  ; @param (namespaced maps in vector) products
  ;  [{:product/count (integer)
  ;    :product/id (string)}]
  ;
  ; @return (integer)
  [products]
  (letfn [(f [result product] (+ result (get-product-price product)))]
         (reduce f 0 products)))
