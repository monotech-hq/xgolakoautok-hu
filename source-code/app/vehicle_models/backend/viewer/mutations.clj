
(ns app.vehicle-models.backend.viewer.mutations
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [mid-fruits.vector                     :as vector]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-model-links!
  ; @param (string) model-id
  ;
  ; @return (?)
  [model-id]
  (letfn [(f [document] (update document :category/models #(vector/remove-item document {:model/id model-id})))]
         (mongo-db/apply-documents! "vehicle_categories" f {})))

(defn remove-type-documents!
  ; @param (string) model-id
  ;
  ; @return (strings in vector)
  [model-id]
  (letfn [(f [result {:type/keys [id] :as type-link}] (conj result (mongo-db/remove-document! "vehicle_types" id)))]
         (let [{:model/keys [types] :as document} (mongo-db/get-document-by-id "vehicle_models" model-id)]
              (reduce f [] types))))

(defn delete-item-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)}
  ;
  ; @return (string)
  [_ {:keys [item-id]}]
  (do ; A törlendő járműmodell hivatkozásainak eltávolítása a kategóriák dokumentumaiból
      (remove-model-links!                item-id)
      ; A törlendő járműmodellhez tartozó járműtípusok törlése
      (remove-type-documents!             item-id)
      ; A törlendő járműmodell dokumentumának törlése
      (mongo-db/remove-document! "vehicle_models" item-id)))

(defmutation delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [env mutation-props]
             {::pathom.co/op-name 'vehicle-models.viewer/delete-item!}
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
  (mongo-db/insert-document! "vehicle_models" item))

(defmutation undo-delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'vehicle-models.viewer/undo-delete-item!}
             (undo-delete-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-type-documents!
  ; @param (string) model-id
  ; @param (string) copy-id
  [model-id copy-id]
  (letfn [; A járműmodellről készült másolat azonosítójának hozzáadása a járműtípusról készült másolat dokumentumához
          (prepare-f [document] (assoc document :type/model-id copy-id))
          (f [result {:type/keys [id] :as type-link}]
             (let [{:type/keys [id] :as copy} (mongo-db/duplicate-document! "vehicle_types" id {:prepare-f prepare-f})]
                  (conj result {:type/id id})))]
         (let [{:model/keys [types] :as document} (mongo-db/get-document-by-id "vehicle_models" model-id)]
              (reduce f [] types))))

(defn duplicate-item-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:item-id (string)}
  ;
  ; @return (namespaced map)
  [{:keys [request]} {:keys [item-id]}]
  (letfn [; A járműmodellhez tartozó típusokról készült másolatok hivatkozásainak hozzáadása a járműmodellről
          ; készült másolat dokumentumához
          (prepare-f [document] (let [copy-id (get document :model/id)
                                      types   (duplicate-type-documents! item-id copy-id)
                                      copy    (common/duplicated-document-prototype request document)]
                                     (assoc copy :model/types types)))]
         ; A duplikálandó járműmodell másolatának elkészítése
         (mongo-db/duplicate-document! "vehicle_models" item-id {:prepare-f prepare-f})))

(defmutation duplicate-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [env mutation-props]
             {::pathom.co/op-name 'vehicle-models.viewer/duplicate-item!}
             (duplicate-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-item! duplicate-item! undo-delete-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
