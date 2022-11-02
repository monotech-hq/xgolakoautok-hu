
(ns app.price-quote-templates.backend.editor.mutations
    (:require [app.common.backend.api                              :as common]
              [app.price-quote-templates.backend.editor.prototypes :as editor.prototypes]
              [com.wsscode.pathom3.connect.operation               :as pathom.co :refer [defmutation]]
              [mongo-db.api                                        :as mongo-db]
              [pathom.api                                          :as pathom]))

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
  (letfn [(prepare-f [document] (->> document (common/added-document-prototype request)
                                              (editor.prototypes/added-price-quote-template-item-prototype)))]
         (mongo-db/save-document! "price_quote_templates" item {:prepare-f prepare-f})))

(defmutation add-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'price-quote-templates.editor/add-item!}
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
  (letfn [(prepare-f [document] (->> document (common/updated-document-prototype request)
                                              (editor.prototypes/updated-price-quote-template-item-prototype)))]
         (mongo-db/save-document! "price_quote_templates" item {:prepare-f prepare-f})))

(defmutation save-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'price-quote-templates.editor/save-item!}
             (save-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [add-item! save-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
