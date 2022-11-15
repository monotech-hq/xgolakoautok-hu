
(ns app.schemes.backend.form-handler.side-effects
    (:require [app.common.backend.api                      :as common]
              [app.schemes.backend.form-handler.prototypes :as form-handler.prototypes]
              [mongo-db.api                                :as mongo-db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-form!
  ; @param (map) request
  ;  {:session (map)}
  ; @param (keyword) scheme-id
  ; @param (namespaced map) scheme-item
  ;  {:scheme/fields (namespaced maps in vector)(opt)
  ;   [{:field/field-id (keyword)(opt
  ;     :field/field-no (string)(opt)
  ;     :field/name (metamorphic-content)
  ;     :field/protected? (boolean)(opt)
  ;     :field/type (keyword)(opt)
  ;      :single-field, :multi-field
  ;      Default: :single-field
  ;     :field/unit (metamorphic-content)(opt)}]
  ;    :scheme/scheme-id (keyword)(opt)}
  ;
  ; @usage
  ;  (save-form! {...} :my-scheme {...})
  ;
  ; @return (namespaced map)
  ;  {:scheme/id (string)}
  [request scheme-id scheme-item]
  (if-let [scheme-exists? (mongo-db/get-document-by-query "schemes" {:scheme/scheme-id scheme-id})]
          ; Ha a scheme-id azonosítójú scheme már létezik ...
          (letfn [(prepare-f [document] (->> document (common/updated-document-prototype request)
                                                      (form-handler.prototypes/scheme-item-prototype scheme-id)))]
                 (mongo-db/save-document! "schemes" scheme-item {:prepare-f prepare-f}))
          ; Ha a scheme-id azonosítójú scheme még NEM létezik ...
          (letfn [(prepare-f [document] (->> document (common/added-document-prototype request)
                                                      (form-handler.prototypes/scheme-item-prototype scheme-id)))]
                 (mongo-db/save-document! "schemes" scheme-item {:prepare-f prepare-f}))))
