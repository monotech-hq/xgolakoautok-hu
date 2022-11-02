
(ns app.packages.backend.viewer.mutations
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
  ; @return (namespaced map)
  [_ {:keys [item-id]}]
  (mongo-db/remove-document! "packages" item-id))

(defmutation delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'packages.viewer/delete-item!}
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
  (mongo-db/insert-document! "packages" item))

(defmutation undo-delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'packages.viewer/undo-delete-item!}
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
  (let [prepare-f #(common/duplicated-document-prototype request %)]
       (mongo-db/duplicate-document! "packages" item-id {:prepare-f prepare-f})))

(defmutation duplicate-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [env mutation-props]
             {::pathom.co/op-name 'packages.viewer/duplicate-item!}
             (duplicate-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-package-products-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:package-id (string)
  ;   :package-products (maps in vector)}
  ;
  ; @return (namespaced map)
  [{:keys [request]} {:keys [package-id package-products]}]
  (let [prepare-f #(common/updated-document-prototype request %)]
       (mongo-db/apply-document! "packages" package-id #(assoc % :package/products package-products)
                                                        {:prepare-f prepare-f})))

(defmutation save-package-products!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:package-id (string)
             ;   :package-products (maps in vector)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'packages.viewer/save-package-products!}
             (save-package-products-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-package-services-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:package-id (string)
  ;   :package-services (maps in vector)}
  ;
  ; @return (namespaced map)
  [{:keys [request]} {:keys [package-id package-services]}]
  (let [prepare-f #(common/updated-document-prototype request %)]
       (mongo-db/apply-document! "packages" package-id #(assoc % :package/services package-services)
                                                        {:prepare-f prepare-f})))

(defmutation save-package-services!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:package-id (string)
             ;   :package-services (maps in vector)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'packages.viewer/save-package-services!}
             (save-package-services-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-item! duplicate-item! undo-delete-item! save-package-products! save-package-services!])

(pathom/reg-handlers! ::handlers HANDLERS)
