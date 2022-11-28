
(ns app.common.frontend.item-picker.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn picker-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (map)
  ;  {:export-filter-f (function)
  ;   :import-id-if (function)}
  [_ picker-props]
  (merge {:export-filter-f (fn [item-id] {:id item-id})
          :import-id-f     (fn [{:keys [id]}] id)}
         (param picker-props)
         ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
         (if (:read-only? picker-props)
             {:sortable? false})))
