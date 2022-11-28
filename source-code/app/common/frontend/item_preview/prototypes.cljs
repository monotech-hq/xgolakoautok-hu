
(ns app.common.frontend.item-preview.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;
  ; @return (map)
  ;  {:import-id-if (function)}
  [_ preview-props]
  (merge {:import-id-f (fn [{:keys [id]}] id)}
         (param preview-props)))
