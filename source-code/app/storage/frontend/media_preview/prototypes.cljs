
(ns app.storage.frontend.media-preview.prototypes
    (:require [candy.api  :refer [param]]
              [vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:item-link (namespaced map)(opt)
  ;    {:media/id (string)}}
  ;
  ; @return (map)
  ;  {:import-id-f (function)
  ;   :item-id (string)
  ;   :item-path (vector)
  ;   :thumbnail (map)
  ;   :transfer-id (keyword)}
  [_ {{:media/keys [id]} :item-link :keys [thumbnail] :as preview-props}]
  (merge {:thumbnail (merge {:height :5xl :width :5xl} thumbnail)}
         (param preview-props)
         {:import-id-f :media/id
          :item-path   [:storage :media-preview/downloaded-items id]
          :transfer-id :storage.media-preview}))
