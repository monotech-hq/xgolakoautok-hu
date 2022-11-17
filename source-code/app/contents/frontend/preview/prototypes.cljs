
(ns app.contents.frontend.preview.prototypes
    (:require [candy.api  :refer [param]]
              [vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  ;
  ; @return (map)
  ;  {:font-size (keyword)
  ;   :font-weight (keyword)
  ;   :items (namespaced maps in vector)}
  [_ {:keys [items] :as preview-props}]
  (merge {:font-size   :s
          :font-weight :normal}
         (param preview-props)
         {:items (vector/remove-items-by items nil?)}))
