
(ns app.rental-vehicles.backend.viewer.mutations
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)}
  ;
  ; @return (string)
  [_ {:keys [item-id]}]
  (mongo-db/remove-document! "rental_vehicles" item-id {:ordered? true}))

(defmutation delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [env mutation-props]
             {::pathom.co/op-name 'rental-vehicles.viewer/delete-item!}
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
  (mongo-db/insert-document! "rental_vehicles" item {:ordered? true}))

(defmutation undo-delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'rental-vehicles.viewer/undo-delete-item!}
             (undo-delete-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-item-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:item-id (string)}
  ;
  ; @return (string)
  [{:keys [request]} {:keys [item-id]}]
  (let [prepare-f #(common/duplicated-document-prototype request %)]
       (mongo-db/duplicate-document! "rental_vehicles" item-id {:ordered? true :prepare-f prepare-f})))

(defmutation duplicate-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [env mutation-props]
             {::pathom.co/op-name 'rental-vehicles.viewer/duplicate-item!}
             (duplicate-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-item! duplicate-item! undo-delete-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
