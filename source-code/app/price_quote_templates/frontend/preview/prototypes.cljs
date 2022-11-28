
(ns app.price-quote-templates.frontend.preview.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:item-link (namespaced map)(opt)
  ;    {:template/id (string)}}
  ;
  ; @return (map)
  ;  {:import-id-f (function)
  ;   :item-id (string)
  ;   :item-path (vector)
  ;   :transfer-id (keyword)}
  [_ {{:template/keys [id]} :item-link :as preview-props}]
  (merge {}
         (param preview-props)
         {:import-id-f :template/id
          :item-path   [:price-quote-templates :preview/downloaded-items id]
          :transfer-id :price-quote-templates.preview}))
