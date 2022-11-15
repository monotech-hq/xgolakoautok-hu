
(ns app.storage.frontend.media-picker.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {}
  ;
  ; @return (map)
  ;  {:disabled? (boolean)
  ;   :items (namespaced maps in vector)
  ;   :indent (map)
  ;   :placeholder (metamorphic-content)
  ;   :sortable? (boolean)
  ;   :thumbnail (map)
  ;   :value-path (vector)}
  [_ {:keys [disabled? multi-select? placeholder sortable? thumbnail value-path]}]
  ; XXX#6071 (app.products.frontend.picker.prototypes)
  (let [picked-media @(r/subscribe [:x.db/get-item value-path])]
       {:disabled?   disabled?
        :indent      {:top :m}
        :items       (cond multi-select? picked-media picked-media [picked-media])
        :placeholder placeholder
        :sortable?   sortable?
        :thumbnail   thumbnail
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
