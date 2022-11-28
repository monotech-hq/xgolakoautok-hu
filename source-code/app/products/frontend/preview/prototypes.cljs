
(ns app.products.frontend.preview.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:item-link (namespaced map)(opt)
  ;    {:product/id (string)}}
  ;
  ; @return (map)
  ;  {:import-id-f (function)
  ;   :item-id (string)
  ;   :item-path (vector)
  ;   :transfer-id (keyword)}
  [_ {{:product/keys [id]} :item-link :as preview-props}]
  (merge {}
         (param preview-props)
         {:import-id-f :product/id
          :item-path   [:products :preview/downloaded-items id]
          :transfer-id :products.preview}))
