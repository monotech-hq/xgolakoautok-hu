
(ns app.clients.backend.lister.mutations
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [vector.api                     :as vector]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-items-f
  [_ {:keys [item-ids]}]
  (mongo-db/remove-documents! "clients" item-ids))

(defmutation delete-items!
             [env mutation-props]
             {::pathom.co/op-name 'clients.lister/delete-items!}
             (delete-items-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-items-f
  [_ {:keys [items]}]
  ; XXX#7601
  ; A :client/name virtuális mezőt szükséges eltávolítani a dokumentumokból!
  (letfn [(f [item] (dissoc item :client/name))]
         (mongo-db/insert-documents! "clients" (vector/->items items f))))

(defmutation undo-delete-items!
             [env mutation-props]
             {::pathom.co/op-name 'clients.lister/undo-delete-items!}
             (undo-delete-items-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-items-f
  [{:keys [request]} {:keys [item-ids]}]
  (let [prepare-f #(common/duplicated-document-prototype request %)]
       (mongo-db/duplicate-documents! "clients" item-ids {:prepare-f prepare-f})))

(defmutation duplicate-items!
             [env mutation-props]
             {::pathom.co/op-name 'clients.lister/duplicate-items!}
             (duplicate-items-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-items! undo-delete-items! duplicate-items!])

(pathom/reg-handlers! ::handlers HANDLERS)
