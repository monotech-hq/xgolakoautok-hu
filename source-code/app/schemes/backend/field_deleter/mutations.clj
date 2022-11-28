
(ns app.schemes.backend.field-deleter.mutations
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [vector.api                            :as vector]
              [x.user.api                            :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-field-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:collection-names (strings in vector)
  ;   :field-id (keyword)
  ;   :scheme-id (keyword)}
  [{:keys [request]} {:keys [collection-names field-id scheme-id]}]
  ; TODO
  ; A mezőben tárolt adatokat is törölni kell a collection-names vektorban átadott
  ; nevekhez tartozó kollekciókban!
  (if (x.user/request->authenticated? request)
      (if-let [scheme (mongo-db/get-document-by-query "schemes" {:scheme/scheme-id scheme-id})]
              (letfn [(f [field-props] (not= field-id (:field/field-id field-props)))]
                     (let [scheme (update scheme :scheme/fields vector/filter-items-by f)]
                          (mongo-db/save-document! "schemes" scheme))))))

(defmutation delete-field!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:collection-names (strings in vector)
             ;   :field-id (keyword)
             ;   :scheme-id (keyword)}
             [env mutation-props]
             {::pathom.co/op-name 'schemes.field-deleter/delete-field!}
             (delete-field-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-field!])

(pathom/reg-handlers! ::handlers HANDLERS)
