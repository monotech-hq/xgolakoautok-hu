
(ns app.vehicle-types.frontend.picker.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) type-link
  ;
  ; @return (map)
  ;  {:item-link (namespaced map)}
  [_ picker-props type-link]
  (merge (select-keys picker-props [:disabled? :placeholder])
         {:item-link type-link}))

(defn picker-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:multi-select? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:export-filter-f (function)
  ;   :import-id-f (function)
  ;   :on-select (metamorphic-event)
  ;   :toggle-label (metamorphic-content)
  ;   :transfer-id (keyword)}
  [_ {:keys [multi-select?] :as picker-props}]
  (merge {:toggle-label    (if multi-select? :select-vehicle-types! :select-vehicle-type!)}
         (param picker-props)
         {:export-filter-f (fn [type-id] {:type/id type-id})
          :import-id-f     :type/id
          :items-path      [:vehicle-types :picker/downloaded-items]
          :on-select       [:vehicle-types.selector/load-selector! :vehicle-types.selector picker-props]
          :transfer-id     :vehicle-types.lister}))
