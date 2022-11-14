
(ns app.price-quotes.frontend.editor.helpers
    (:require [mid-fruits.candy  :refer [return]]
              [math.api   :as math]
              [mid-fruits.mixed  :as mixed]
              [mid-fruits.string :as string]
              [re-frame.api      :as r]
              [time.api          :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn initial-release-date
  ; @example
  ;  (editor.helpers/initial-release-date)
  ;  =>
  ;  "2022-04-20"
  ;
  ; @return (string)
  []
  (let [server-date @(r/subscribe [:db/get-item [:price-quotes :editor/server-date]])]
       (return server-date)))

(defn release-date-from
  ; @example
  ;  (editor.helpers/release-date-from)
  ;  =>
  ;  "2022-01-01"
  ;
  ; @return (string)
  []
  (if-let [release-date @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :release-date]])]
          (let [release-year (-> release-date time/timestamp-string->year string/to-integer)]
               (str release-year"-01-01"))))

(defn release-date-to
  ; @example
  ;  (editor.helpers/release-date-to)
  ;  =>
  ;  "2022-12-31"
  ;
  ; @return (string)
  []
  (if-let [release-date @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :release-date]])]
          (let [release-year (-> release-date time/timestamp-string->year string/to-integer)]
               (str release-year"-12-31"))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-net-total-price
  ; @return (integer)
  []
  (let [vehicle-unit-price      @(r/subscribe [:db/get-item [:price-quotes :editor/vehicle-unit-price]])
        vehicle-unique-price    @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :vehicle-unique-price]])
        vehicle-unique-pricing? @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :vehicle-unique-pricing?]])
        packages-price          @(r/subscribe [:db/get-item [:price-quotes :editor/packages-price]])
        products-price          @(r/subscribe [:db/get-item [:price-quotes :editor/products-price]])
        vehicle-count           @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :vehicle-count]])
        vehicle-unique-price     (mixed/to-number vehicle-unique-price)
        vehicle-price            (* vehicle-count (if vehicle-unique-pricing? vehicle-unique-price vehicle-unit-price))]
       (+ vehicle-price packages-price products-price)))

(defn get-total-vat
  []
  (let [vat-value      @(r/subscribe [:user/get-user-settings-item :vat-value])
        net-total-price (get-net-total-price)
        total-vat       (math/percent-result net-total-price vat-value)]
       (math/round total-vat)))

(defn get-gross-total-price
  []
  (let [net-total-price   (get-net-total-price)
        total-vat         (get-total-vat)
        gross-total-price (+ net-total-price total-vat)]
       (math/round gross-total-price)))
