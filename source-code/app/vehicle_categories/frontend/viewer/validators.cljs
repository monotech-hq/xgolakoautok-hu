
(ns app.vehicle-categories.frontend.viewer.validators
    (:require [mid-fruits.map :as map]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-category-models-response-valid?
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ server-response]]
  (let [mutation-name   (symbol :vehicle-categories.viewer/save-category-models!)
        mutation-answer (get server-response mutation-name)]
       (map/nonempty? mutation-answer)))
