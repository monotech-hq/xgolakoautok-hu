
(ns app.packages.frontend.preview.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:item-link (namespaced map)(opt)
  ;    {:package/id (string)}}
  ;
  ; @return (map)
  ;  {:import-id-f (function)
  ;   :item-id (string)
  ;   :item-path (vector)
  ;   :transfer-id (keyword)}
  [_ {{:package/keys [id]} :item-link :as preview-props}]
  (merge {}
         (param preview-props)
         {:import-id-f :package/id
          :item-path   [:packages :preview/downloaded-items id]
          :transfer-id :packages.preview}))
