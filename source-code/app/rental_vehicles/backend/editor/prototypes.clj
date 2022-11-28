
(ns app.rental-vehicles.backend.editor.prototypes
    (:require [app.rental-vehicles.mid.handler.helpers :as handler.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn added-document-prototype
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  [{:vehicle/keys [name ] :as document}]
  (merge document {:vehicle/link-name (handler.helpers/vehicle-link-name name)}))

(defn updated-document-prototype
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  [document]
  (added-document-prototype))
