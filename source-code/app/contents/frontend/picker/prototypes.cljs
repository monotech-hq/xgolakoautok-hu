
(ns app.contents.frontend.picker.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  ; @param (namespaced map) content-link
  ;
  ; @return (map)
  ;  {:disabled? (boolean)
  ;   :item-link (namespaced map)
  ;   :placeholder (metamorphic-content)}
  [_ {:keys [color disabled? placeholder]} content-link]
  {:color       color
   :disabled?   disabled?
   :item-link   content-link
   :placeholder placeholder})

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
  (merge {:toggle-label    (if multi-select? :select-contents! :select-content!)}
         (param picker-props)
         {:export-filter-f (fn [content-id] {:content/id content-id})
          :import-id-f     :content/id
          :items-path      [:contents :picker/downloaded-items]
          :on-select       [:contents.selector/load-selector! :contents.selector picker-props]
          :transfer-id     :contents.lister}))
