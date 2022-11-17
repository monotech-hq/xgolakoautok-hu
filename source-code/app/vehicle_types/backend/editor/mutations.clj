
(ns app.vehicle-types.backend.editor.mutations
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [vector.api                            :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-item-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:item (namespaced map)}
  ;
  ; @return (namespaced map)
  [{:keys [request]} {:keys [item]}]
  (let [type-id (mongo-db/generate-id)]
       ; Típus hivatkozásának hozzáadása a modell dokumentumához
       (let [prepare-f #(common/updated-document-prototype request %)
             model-id   (:type/model-id item)]
            (letfn [(f [%] (update % :model/types vector/conj-item {:type/id type-id}))]
                   (mongo-db/apply-document! "vehicle_models" model-id f {:prepare-f prepare-f})))
       ; Típus dokumentumának mentése
       (let [prepare-f #(common/added-document-prototype request %)]
            (mongo-db/save-document! "vehicle_types" (assoc item :type/id type-id) {:prepare-f prepare-f}))))

(defmutation add-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'vehicle-types.editor/add-item!}
             (add-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-item-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:item (namespaced map)}
  ;
  ; @return (namespaced map)
  [{:keys [request]} {:keys [item]}]
  (let [prepare-f #(common/updated-document-prototype request %)]
       (mongo-db/save-document! "vehicle_types" item {:prepare-f prepare-f})))

(defmutation save-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'vehicle-types.editor/save-item!}
             (save-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [add-item! save-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
