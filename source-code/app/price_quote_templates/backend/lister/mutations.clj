
(ns app.price-quote-templates.backend.lister.mutations
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-items-f
  [_ {:keys [item-ids]}]
  (mongo-db/remove-documents! "price_quote_templates" item-ids))

(defmutation delete-items!
             [env mutation-props]
             {::pathom.co/op-name 'price-quote-templates.lister/delete-items!}
             (delete-items-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-items-f
  [_ {:keys [items]}]
  (mongo-db/insert-documents! "price_quote_templates" items))

(defmutation undo-delete-items!
             [env mutation-props]
             {::pathom.co/op-name 'price-quote-templates.lister/undo-delete-items!}
             (undo-delete-items-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-items-f
  [{:keys [request]} {:keys [item-ids]}]
  (let [prepare-f #(common/duplicated-document-prototype request %)]
       (mongo-db/duplicate-documents! "price_quote_templates" item-ids {:prepare-f prepare-f})))

(defmutation duplicate-items!
             [env mutation-props]
             {::pathom.co/op-name 'price-quote-templates.lister/duplicate-items!}
             (duplicate-items-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-items! undo-delete-items! duplicate-items!])

(pathom/reg-handlers! ::handlers HANDLERS)
