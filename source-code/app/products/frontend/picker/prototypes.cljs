
(ns app.products.frontend.picker.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) product-link
  ;
  ; @return (map)
  ;  {:item-link (namespaced map)}
  [_ picker-props product-link]
  (merge (select-keys picker-props [:disabled? :placeholder])
         {:item-link product-link}))

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
  (merge {:toggle-label    (if multi-select? :select-products! :select-product!)}
         (param picker-props)
         {:export-filter-f (fn [product-id] {:product/id product-id})
          :import-id-f     :product/id
          :items-path      [:products :picker/downloaded-items]
          :on-select       [:products.selector/load-selector! :products.selector picker-props]
          :transfer-id     :products.lister}))
