
(ns app.packages.frontend.picker.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) package-link
  ;
  ; @return (map)
  ;  {:item-link (namespaced map)}
  [_ picker-props package-link]
  (merge (select-keys picker-props [:disabled? :placeholder])
         {:item-link package-link}))

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
  (merge {:toggle-label    (if multi-select? :select-packages! :select-package!)}
         (param picker-props)
         {:export-filter-f (fn [package-id] {:package/id package-id})
          :import-id-f     :package/id
          :items-path      [:packages :picker/downloaded-items]
          :on-select       [:packages.selector/load-selector! :packages.selector picker-props]
          :transfer-id     :packages.lister}))
