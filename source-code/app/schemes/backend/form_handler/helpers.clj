
(ns app.schemes.backend.form-handler.helpers
    (:require [mongo-db.api :as mongo-db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn form-exists?
  ; @param (keyword) scheme-id
  ;
  ; @usage
  ;  (form-exists? :my-scheme)
  ;
  ; @return (boolean)
  [scheme-id]
  (let [scheme (mongo-db/get-document-by-query "schemes" {:scheme/scheme-id scheme-id})]
       (some? scheme)))

(defn field-exists?
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ;
  ; @usage
  ;  (field-exists? :my-scheme :my-field)
  ;
  ; @return (boolean)
  [scheme-id field-id]
  (if-let [{:scheme/keys [fields]} (mongo-db/get-document-by-query "schemes" {:scheme/scheme-id scheme-id})]
          (letfn [(f [field-item] (= field-id (:field/field-id field-item)))]
                 (some f fields))))
