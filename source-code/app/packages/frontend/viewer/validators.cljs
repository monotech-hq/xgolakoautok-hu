
(ns app.packages.frontend.viewer.validators
    (:require [map.api :as map]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-package-products-response-valid?
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ server-response]]
  (let [mutation-name   (symbol :packages.viewer/save-package-products!)
        mutation-answer (get server-response mutation-name)]
       (map/nonempty? mutation-answer)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-package-services-response-valid?
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ server-response]]
  (let [mutation-name   (symbol :packages.viewer/save-package-services!)
        mutation-answer (get server-response mutation-name)]
       (map/nonempty? mutation-answer)))
