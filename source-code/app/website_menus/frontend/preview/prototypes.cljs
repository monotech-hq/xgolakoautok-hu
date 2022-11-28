
(ns app.website-menus.frontend.preview.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:item-link (namespaced map)(opt)
  ;    {:menu/id (string)}}
  ;
  ; @return (map)
  ;  {:import-id-f (function)
  ;   :item-id (string)
  ;   :item-path (vector)
  ;   :transfer-id (keyword)}
  [_ {{:menu/keys [id]} :item-link :as preview-props}]
  (merge {}
         (param preview-props)
         {:import-id-f :menu/id
          :item-path   [:website-menus :preview/downloaded-items id]
          :transfer-id :website-menus.preview}))
