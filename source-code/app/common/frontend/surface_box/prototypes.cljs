
(ns app.common.frontend.surface-box.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-props-prototype
  ; @param (map) box-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [{:keys [icon] :as box-props}]
  (merge {}
         (if icon {:icon-family :material-icons-filled})
         (param box-props)))
