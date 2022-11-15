
(ns app.contents.frontend.picker.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :multi-select? (boolean)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :sortable? (boolean)(opt)
  ;   :value-path (vector)}
  ;
  ; @return (map)
  ;  {:color (keyword or string)
  ;   :disabled? (boolean)
  ;   :items (namespaced maps in vector)
  ;   :indent (map)
  ;   :placeholder (metamorphic-content)
  ;   :sortable? (boolean)
  ;   :value-path (vector)}
  [_ {:keys [disabled? multi-select? placeholder sortable? value-path]}]
  ; XXX#6071 (app.products.frontend.picker.prototypes)
  (let [picked-contents @(r/subscribe [:x.db/get-item value-path])]
       {:color       :muted
        :disabled?   disabled?
        :indent      {:top :m}
        :items       (cond multi-select? picked-contents picked-contents [picked-contents])
        :placeholder placeholder
        :sortable?   sortable?
        :value-path  value-path}))

(defn picker-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (map)
  ;  {}
  [_ picker-props]
  (merge {}
         (param picker-props)))
