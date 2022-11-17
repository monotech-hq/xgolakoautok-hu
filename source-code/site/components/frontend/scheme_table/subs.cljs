
(ns site.components.frontend.scheme-table.subs
    (:require [candy.api    :refer [return]]
              [re-frame.api :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-scheme-fields
  ; @param (keyword) scheme-id
  ;
  ; @return (namespaced maps in vector)
  [db [_ scheme-id]]
  (get-in db [:components :scheme-table/scheme-forms scheme-id :scheme/fields]))

(defn get-scheme-field
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ;
  ; @return (namespaced map)
  [db [_ scheme-id field-id]]
  (let [scheme-fields (r get-scheme-fields db scheme-id)]
       (letfn [(f [field-props] (if (= field-id (:field/field-id field-props))
                                    (return field-props)))]
              (some f scheme-fields))))

(defn get-scheme-field-ids
  ; @param (keyword) scheme-id
  ;
  ; @return (keywords in vector)
  [db [_ scheme-id]]
  (let [scheme-fields (r get-scheme-fields db scheme-id)]
       (letfn [(f [%1 %2] (conj %1 (:field/field-id %2)))]
              (reduce f [] scheme-fields))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :components.scheme-table/get-scheme-fields    get-scheme-fields)
(r/reg-sub :components.scheme-table/get-scheme-field     get-scheme-field)
(r/reg-sub :components.scheme-table/get-scheme-field-ids get-scheme-field-ids)
