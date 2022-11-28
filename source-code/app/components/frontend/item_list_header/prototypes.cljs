
(ns app.components.frontend.item-list-header.prototypes
    (:require [candy.api  :refer [param]]
              [vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @param (map) header-props
  ;  {:cells (vector)}
  ;
  ; @return (map)
  ;  {:cells (vector)}
  [{:keys [cells] :as header-props}]
  ; XXX#0561 (source-code/app/components/frontend/item-list-row/prototypes.cljs)
  (merge {}
         (param header-props)
         {:cells (vector/remove-items-by cells nil?)}))

(defn cell-props-prototype
  ; @param (map) cell-props
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [order-by]} _ {:keys [order-by-key label] :as cell-props}]
  (let [order-by? (and order-by order-by-key (= (name order-by-key) (namespace order-by)))]
       (merge {:color         (if order-by? :default :muted)
               :icon-position (if order-by? :right)
               :icon          (if order-by? (case (name order-by) "descending" :arrow_drop_down "ascending" :arrow_drop_up))
               :font-size     :xs
               :line-height   :block}
              (param cell-props)
              {:content label})))
