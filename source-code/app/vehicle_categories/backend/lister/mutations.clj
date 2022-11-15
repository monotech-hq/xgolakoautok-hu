
(ns app.vehicle-categories.backend.lister.mutations
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
  (mongo-db/save-documents! "vehicle_categories" items {:ordered? true}))

(defmutation reorder-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env mutation-props]
             {::pathom.co/op-name 'vehicle-categories.lister/reorder-items!}
             (reorder-items-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-items-f
  [{:keys [item-ids]}]
  (mongo-db/remove-documents! "vehicle_categories" item-ids {:ordered? true}))

(defmutation delete-items!
             [mutation-props]
             {::pathom.co/op-name 'vehicle-categories.lister/delete-items!}
             (delete-items-f mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-items-f
  [_ {:keys [items]}]
  (mongo-db/insert-documents! "vehicle_categories" items {:ordered? true}))

(defmutation undo-delete-items!
             [env mutation-props]
             {::pathom.co/op-name 'vehicle-categories.lister/undo-delete-items!}
             (undo-delete-items-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-items-f
  [{:keys [request]} {:keys [item-ids]}]
  (let [prepare-f #(common/duplicated-document-prototype request %)]
       (mongo-db/duplicate-documents! "vehicle_categories" item-ids {:ordered? true :prepare-f prepare-f})))

(defmutation duplicate-items!
             [env mutation-props]
             {::pathom.co/op-name 'vehicle-categories.lister/duplicate-items!}
             (duplicate-items-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-items! undo-delete-items! duplicate-items! reorder-items!])

(pathom/reg-handlers! ::handlers HANDLERS)
