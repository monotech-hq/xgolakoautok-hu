
(ns app.clients.frontend.picker.prototypes
    (:require [mid-fruits.candy :refer [param]]
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
  (let [picked-clients @(r/subscribe [:db/get-item value-path])]
       {:disabled?   disabled?
        :items       (cond multi-select? picked-clients picked-clients [picked-clients])
        :indent      {:top :m}
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
