
(ns app.clients.frontend.preview.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:item-link (namespaced map)(opt)
  ;    {:client/id (string)}}
  ;
  ; @return (map)
  ;  {:import-id-f (function)
  ;   :item-id (string)
  ;   :item-path (vector)
  ;   :transfer-id (keyword)}
  [_ {{:client/keys [id]} :item-link :as preview-props}]
  (merge {}
         (param preview-props)
         {:import-id-f :client/id
          :item-path   [:clients :preview/downloaded-items id]
          :transfer-id :clients.preview}))
