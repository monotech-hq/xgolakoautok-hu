
(ns templates.price-quotes.blank.items
    (:require [mid-fruits.vector :as vector]
              [re-frame.api      :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-row
  ; @param (map) template-props
  ; @param (map) item
  [_ {:keys [product-description quantity unit-price total-price]}]
  [:tr {:style {:color "#666"}}
       [:td [:h5 product-description]]
       [:td [:h5 {:style {:text-align :right :width "80px"}} quantity]]
       [:td [:h5 {:style {:text-align :right :width "80px"}} unit-price]]
       [:td [:h5 {:style {:text-align :right :width "80px"}} total-price]]])

(defn- items
  ; @param (map) template-props
  ;  {}
  [{:keys [items] :as template-props} dex-from dex-to]
  (letfn [(f [result item]
             (conj result (item-row template-props item)))]
         (reduce f [:tbody] (vector/ranged-items items dex-from dex-to))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (map) template-props
  ;  {}
  ; @param (integer) dex-from
  ; @param (integer) dex-to
  [{:keys [language] :as template-props} dex-from dex-to]
  [:table {:class :blk-items}
          [:thead {:class :blk-items--header}
                  [:tr [:td                                             [:h5 [:strong @(r/subscribe [:dictionary/look-up :product-description {:language language}])]]]
                       [:td {:style {:text-align :right :width "80px"}} [:h5 [:strong @(r/subscribe [:dictionary/look-up :quantity            {:language language}])]]]
                       [:td {:style {:text-align :right :width "80px"}} [:h5 [:strong @(r/subscribe [:dictionary/look-up :unit-price          {:language language}])]]]
                       [:td {:style {:text-align :right :width "80px"}} [:h5 [:strong @(r/subscribe [:dictionary/look-up :amount-price        {:language language}])]]]]]
          (items template-props dex-from dex-to)])
