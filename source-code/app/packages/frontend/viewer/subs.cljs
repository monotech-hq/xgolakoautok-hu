
(ns app.packages.frontend.viewer.subs
    (:require [re-frame.api :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-product-count
  ; @param (string) product-id
  ;
  ; @return (integer)
  [db [_ product-id]]
  (let [package-products (get-in db [:packages :viewer/viewed-item :products])]
       (letfn [(f [{:product/keys [count id]}] (if (= id product-id) count))]
              (some f package-products))))

(defn get-service-count
  ; @param (string) service-id
  ;
  ; @return (integer)
  [db [_ service-id]]
  (let [package-services (get-in db [:packages :viewer/viewed-item :services])]
       (letfn [(f [{:service/keys [count id]}] (if (= id service-id) count))]
              (some f package-services))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :packages.viewer/get-product-count get-product-count)
(r/reg-sub :packages.viewer/get-service-count get-service-count)
