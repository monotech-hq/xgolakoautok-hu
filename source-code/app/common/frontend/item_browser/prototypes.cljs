
(ns app.common.frontend.item-browser.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:auto-title? (boolean)
  ;   :default-order-by (namespaced keyword)
  ;   :label-key (keyword)}
  [_ body-props]
  (merge {:auto-title?      true
          :label-key        :name
          :default-order-by :modified-at/descending}
         (param body-props)))
