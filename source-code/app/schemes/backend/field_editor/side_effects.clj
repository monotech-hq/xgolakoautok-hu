
(ns app.schemes.backend.field-editor.side-effects
    (:require [app.schemes.backend.form-handler.helpers      :as form-handler.helpers]
              [app.schemes.backend.form-handler.side-effects :as form-handler.side-effects]
              [candy.api                              :refer [return]]
              [mid-fruits.vector                             :as vector]
              [mongo-db.api                                  :as mongo-db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-field!
  ; @param (map) request
  ;  {:session (map)}
  ; @param (keyword) scheme-id
  ; @param (namespaced map) field-item
  ;  {:field/field-id (string)(opt
  ;   :field/field-no (string)(opt)
  ;   :field/name (metamorphic-content)
  ;   :field/undeletable? (boolean)(opt)
  ;   :field/unit (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  (save-field! {...} :my-scheme {...})
  ;
  ; @return (namespaced map)
  ;  {:field/id (string)}
  [request scheme-id field-item]
  (letfn [(f [{:scheme/keys [fields] :as scheme} {:field/keys [field-id] :as field-item}]
             (if (form-handler.helpers/field-exists? scheme-id field-id)
                 ; Ha a field-id azonosítójú mező már létezik ...
                 (letfn [(f [%] (if (= field-id (:field/field-id %))
                                    (return field-item)
                                    (return %)))]
                        (update scheme :scheme/fields vector/->items f))
                 ; Ha a field-id azonosítójú mező még NEM létezik ...
                 (update scheme :scheme/fields vector/conj-item field-item)))]
         (if-let [scheme (mongo-db/get-document-by-query "schemes" {:scheme/scheme-id scheme-id})]
                 ; Ha a scheme-id azonosítójú scheme már létezik ...
                 (let [scheme (f scheme field-item)]
                      (if (form-handler.side-effects/save-form! request scheme-id scheme)
                          (return field-item)))
                 ; Ha a scheme-id azonosítójú scheme még NEM létezik ...
                 (let [scheme {:scheme/fields [field-item] :scheme/scheme-id scheme-id}]
                      (if (form-handler.side-effects/save-form! request scheme-id scheme)
                          (return field-item))))))
