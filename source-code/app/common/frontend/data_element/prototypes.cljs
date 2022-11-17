
(ns app.common.frontend.data-element.prototypes
    (:require [candy.api         :refer [param return]]
              [vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  ; @param (map) element-props
  ;  {:value (metamorphic-content or metamorphic-contents in vector)}
  ;
  ; @return (map)
  ;  {:font-size (keyword)
  ;   :value (metamorphic-contents in vector)}
  [{:keys [value] :as element-props}]
  ; XXX#0516 (app.common.frontend.data-element.views)
  (merge {:font-size :s}
         (param element-props)
         {:value (cond (vector/nonempty? value) (return value)
                       (vector?          value) [nil]
                       :return                  [value])}))
