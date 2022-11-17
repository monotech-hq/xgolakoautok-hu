
(ns app.vehicle-types.backend.viewer.mutations
    (:require [app.common.backend.api                :as common]
              [candy.api                             :refer [return]]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [vector.api                     :as vector]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)}
  ;
  ; @return (namespaced map)
  [_ {:keys [item-id]}]
  (mongo-db/remove-document! "vehicle_types" item-id))
  ; Típus hivatkozásának törlése a modell dokumentumából
  ; TODO

(defmutation delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'vehicle-types.viewer/delete-item!}
             (delete-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-item-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item (namespaced map)}
  ;
  ; @return (namespaced map)
  [_ {:keys [item]}]
  (mongo-db/insert-document! "vehicle_types" item))

(defmutation undo-delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'vehicle-types.viewer/undo-delete-item!}
             (undo-delete-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-item-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:item-id (string)}
  ;
  ; @return (namespaced map)
  [{:keys [request]} {:keys [item-id]}]
  (let [prepare-f #(common/duplicated-document-prototype request %)
        copy-item  (mongo-db/duplicate-document! "vehicle_types" item-id {:prepare-f prepare-f})]
       (let [prepare-f #(common/updated-document-prototype request %)
             model-id   (:type/model-id copy-item)
             copy-id    (:type/id       copy-item)]
            (letfn [(f [%] (update % :model/types vector/conj-item {:type/id copy-id}))]
                   (mongo-db/apply-document! "vehicle_models" model-id f {:prepare-f prepare-f})))
       (return copy-item)))

(defmutation duplicate-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [env mutation-props]
             {::pathom.co/op-name 'vehicle-types.viewer/duplicate-item!}
             (duplicate-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-item! duplicate-item! undo-delete-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
