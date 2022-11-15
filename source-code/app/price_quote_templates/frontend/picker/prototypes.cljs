
(ns app.price-quote-templates.frontend.picker.prototypes
    (:require [candy.api :refer [param]]
              [re-frame.api     :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [disabled? multi-select? placeholder value-path]}]
  ; XXX#6071 (app.products.frontend.picker.prototypes)
  (let [picked-templates @(r/subscribe [:db/get-item value-path])]
       {:disabled?   disabled?
        :indent      {:top :m}
        :items       (cond multi-select? picked-templates picked-templates [picked-templates])
        :placeholder placeholder}))

(defn picker-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (map)
  ;  {}
  [_ picker-props]
  (merge {}
         (param picker-props)))
