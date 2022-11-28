
(ns app.components.frontend.list-item-button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) button-props
  ;
  ; @return (map)
  ;  {}
  [button-props]
  (merge {:background-color :highlight
          :font-size        :xs
          :hover-color      :highlight
          :width            120}
         (param button-props)))
