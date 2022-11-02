
(ns app.contents.frontend.picker.prototypes
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
  (let [picked-contents @(r/subscribe [:db/get-item value-path])]
       {:color       :muted
        :disabled?   disabled?
        :indent      {:top :m}
        :items       (cond multi-select? picked-contents picked-contents [picked-contents])
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
