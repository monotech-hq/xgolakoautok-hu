
(ns app.packages.backend.handler.helpers
    (:require [mid-fruits.candy :refer [return]]
              [mid-fruits.mixed :as mixed]
              [mongo-db.api     :as mongo-db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------


(defn get-package-price
  ; @param (namespaced map) package-link
  ;  {:package/count (integer)
  ;   :package/id (string)}
  ;
  ; @return (integer)
  [{:package/keys [count id]}]
  ; Package automatic price: a csomagban található termékek és szolgáltatások árainak összege
  ; Package static price:    a csomag egyedi ára
  (if-let [{:package/keys [automatic-pricing? price products services]} (mongo-db/get-document-by-id "packages" id)]
          (if automatic-pricing? ; Automatic pricing
                                 (letfn [(f0 [result {:product/keys [count id]}]
                                             (if-let [{:product/keys [price]} (mongo-db/get-document-by-id "products" id)]
                                                     (+      result (mixed/multiply-numbers count price))
                                                     (return result)))
                                         (f1 [result {:service/keys [count id]}]
                                             (if-let [{:service/keys [price]} (mongo-db/get-document-by-id "services" id)]
                                                     (+      result (mixed/multiply-numbers count price))
                                                     (return result)))]
                                        (* (as-> 0 % (reduce f0 % products)
                                                     (reduce f1 % services))
                                           (mixed/to-number count)))
                                 ; Static pricing
                                 (mixed/multiply-numbers count price))
          (return 0)))

(defn get-packages-price
  ; @param (namespaced maps in vector) packages
  ;  [{:package/count (integer)
  ;    :package/id (string)}]
  ;
  ; @return (integer)
  [packages]
  (letfn [(f [result package] (+ result (get-package-price package)))]
         (reduce f 0 packages)))
