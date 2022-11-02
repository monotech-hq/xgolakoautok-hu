
(ns app.storage.frontend.media-picker.prototypes
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
  [_ {:keys [disabled? multi-select? placeholder sortable? value-path]}]
  ; XXX#6071 (app.products.frontend.picker.prototypes)
  (let [picked-media @(r/subscribe [:db/get-item value-path])]
       {:disabled?   disabled?
        :indent      {:top :m}
        :items       (cond multi-select? picked-media picked-media [picked-media])
        :placeholder placeholder
        :sortable?   sortable?
        :value-path  value-path}))

(defn picker-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (map)
  ;  {}
  [picker-id picker-props]
  (merge {:max-count 8}
         (param picker-props)))
