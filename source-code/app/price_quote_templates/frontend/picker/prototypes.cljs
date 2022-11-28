
(ns app.price-quote-templates.frontend.picker.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) template-link
  ;
  ; @return (map)
  ;  {:item-link (namespaced map)}
  [_ picker-props template-link]
  (merge (select-keys picker-props [:disabled? :placeholder])
         {:item-link template-link}))

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
  (merge {:toggle-label    (if multi-select? :select-price-quote-templates! :select-price-quote-template!)}
         (param picker-props)
         {:export-filter-f (fn [template-id] {:template/id template-id})
          :import-id-f     :template/id
          :items-path      [:price-quote-templates :picker/downloaded-items]
          :on-select       [:price-quote-templates.selector/load-selector! :price-quote-templates.selector picker-props]
          :transfer-id     :price-quote-templates.lister}))
