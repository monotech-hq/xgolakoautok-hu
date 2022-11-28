
(ns app.contents.frontend.preview.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:item-link (namespaced map)(opt)
  ;    {:content/id (string)}}
  ;
  ; @return (map)
  ;  {:font-size (keyword)
  ;   :font-weight (keyword)
  ;  :import-id-f (function)
  ;   :item-id (string)
  ;   :item-path (vector)
  ;   :transfer-id (keyword)}
  [_ {{:content/keys [id]} :item-link :as preview-props}]
  (merge {:font-size   :s
          :font-weight :normal}
         (param preview-props)
         {:import-id-f :content/id
          :item-path   [:contents :preview/downloaded-items id]
          :transfer-id :contents.preview}))
