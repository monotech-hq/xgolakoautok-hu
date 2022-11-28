
(ns app.storage.frontend.media-picker.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) media-link
  ;
  ; @return (map)
  ;  {:item-link (namespaced map)}
  [_ picker-props media-link]
  (merge (select-keys picker-props [:disabled? :placeholder])
         {:item-link media-link}))

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
  (merge {:toggle-label    (if multi-select? :select-images! :select-image!)}
         (param picker-props)
         {:export-filter-f (fn [media-id] {:media/id media-id})
          :import-id-f     :media/id
          :items-path      [:storage :picker/downloaded-items]
          :on-select       [:storage.media-selector/load-selector! :storage.media-selector picker-props]
          :transfer-id     :storage.media-lister}))
