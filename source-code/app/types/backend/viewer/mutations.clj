
(ns app.types.backend.viewer.mutations
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [candy.api                      :refer [return]]
              [mid-fruits.vector                     :as vector]
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
  (mongo-db/remove-document! "types" item-id))
  ; Típus hivatkozásának törlése a modell dokumentumából
  ; TODO

(defmutation delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'types.viewer/delete-item!}
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
  (mongo-db/insert-document! "types" item))

(defmutation undo-delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'types.viewer/undo-delete-item!}
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
        copy-item  (mongo-db/duplicate-document! "types" item-id {:prepare-f prepare-f})]
       (let [prepare-f #(common/updated-document-prototype request %)
             model-id   (:type/model-id copy-item)
             copy-id    (:type/id       copy-item)]
            (mongo-db/apply-document! "models" model-id #(update % :model/types vector/conj-item {:type/id copy-id})
                                                         {:prepare-f prepare-f}))
       (return copy-item)))

(defmutation duplicate-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [env mutation-props]
             {::pathom.co/op-name 'types.viewer/duplicate-item!}
             (duplicate-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-item! duplicate-item! undo-delete-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
