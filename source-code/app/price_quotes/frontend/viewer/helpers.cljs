
(ns app.price-quotes.frontend.viewer.helpers
    (:require [candy.api    :refer [return]]
              [math.api     :as math]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-net-total-price
  ; @return (integer)
  []
  (let [vehicle-unit-price      @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-unit-price]])
        vehicle-unique-price    @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-unique-price]])
        vehicle-unique-pricing? @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-unique-pricing?]])
        packages-price          @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :packages-price]])
        products-price          @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :products-price]])
        vehicle-count           @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-count]])
        vehicle-price            (* vehicle-count (if vehicle-unique-pricing? vehicle-unique-price vehicle-unit-price))]
       (+ vehicle-price packages-price products-price)))

(defn get-total-vat
  []
  (let [vat-value      @(r/subscribe [:x.user/get-user-settings-item :vat-value])
        net-total-price (get-net-total-price)
        total-vat       (math/percent-result net-total-price vat-value)]
       (math/round total-vat)))

(defn get-gross-total-price
  []
  (let [net-total-price   (get-net-total-price)
        total-vat         (get-total-vat)
        gross-total-price (+ net-total-price total-vat)]
       (math/round gross-total-price)))
