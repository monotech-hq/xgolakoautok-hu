
(ns app.rental-vehicles.backend.lister.mutations
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reorder-items-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:items (namespaced maps in vector)}
  ;
  ; @return (namespaced maps in vector)
  [{:keys [request]} {:keys [items]}]
  (mongo-db/save-documents! "rental_vehicles" items {:ordered? true}))

(defmutation reorder-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env mutation-props]
             {::pathom.co/op-name 'rental-vehicles.lister/reorder-items!}
             (reorder-items-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-items-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-ids (strings in vector)}
  ;
  ; @return (strings in vector)
  [_ {:keys [item-ids]}]
  (mongo-db/remove-documents! "rental_vehicles" item-ids {:ordered? true}))

(defmutation delete-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [env mutation-props]
             {::pathom.co/op-name 'rental-vehicles.lister/delete-items!}
             (delete-items-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-items-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:items (namespaced maps in vector)}
  ;
  ; @return (namespaced maps in vector)
  [{:keys [items]}]
  (mongo-db/insert-documents! "rental_vehicles" items {:ordered? true}))

(defmutation undo-delete-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env mutation-props]
             {::pathom.co/op-name 'rental-vehicles.lister/undo-delete-items!}
             (undo-delete-items-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-items-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-ids (keywords in vector)}
  ;
  ; @return (keywords in vector)
  [{:keys [request]} {:keys [item-ids]}]
  (let [prepare-f #(common/duplicated-document-prototype request %)]
       (mongo-db/duplicate-documents! "rental_vehicles" item-ids {:ordered? true :prepare-f prepare-f})))

(defmutation duplicate-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-ids (keywords in vector)}
             ;
             ; @return (keywords in vector)
             [env mutation-props]
             {::pathom.co/op-name 'rental-vehicles.lister/duplicate-items!}
             (duplicate-items-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-items! undo-delete-items! duplicate-items! reorder-items!])

(pathom/reg-handlers! ::handlers HANDLERS)
