
(ns app.vehicle-models.frontend.picker.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) model-link
  ;
  ; @return (map)
  ;  {:item-link (namespaced map)}
  [_ picker-props model-link]
  (merge (select-keys picker-props [:disabled? :placeholder])
         {:item-link model-link}))

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
  (merge {:toggle-label    (if multi-select? :select-vehicle-models! :select-vehicle-model!)}
         (param picker-props)
         {:export-filter-f (fn [model-id] {:model/id model-id})
          :import-id-f     :model/id
          :items-path      [:vehicle-models :picker/downloaded-items]
          :on-select       [:vehicle-models.selector/load-selector! :vehicle-models.selector picker-props]
          :transfer-id     :vehicle-models.lister}))
