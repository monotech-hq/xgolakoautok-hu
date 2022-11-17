
(ns app.schemes.frontend.form-handler.subs
    (:require [candy.api    :refer [return]]
              [re-frame.api :as r :refer [r]]
              [vector.api   :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-scheme-form
  ; @param (keyword) scheme-id
  ;
  ; @return (namespaced map)
  [db [_ scheme-id]]
  (get-in db [:schemes :form-handler/scheme-forms scheme-id]))

(defn get-scheme-fields
  ; @param (keyword) scheme-id
  ;
  ; @return (namespaced maps in vector)
  [db [_ scheme-id]]
  (get-in db [:schemes :form-handler/scheme-forms scheme-id :scheme/fields]))

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

(defn get-scheme-field-groups
  ; @param (keyword) scheme-id
  ;
  ; @return (strings in vector)
  [db [_ scheme-id]]
  (let [scheme-fields (r get-scheme-fields db scheme-id)]
       (letfn [(f [%1 %2] (vector/conj-item-once %1 (:field/group %2)))]
              (reduce f [] scheme-fields))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :schemes.form-handler/get-scheme-form         get-scheme-form)
(r/reg-sub :schemes.form-handler/get-scheme-fields       get-scheme-fields)
(r/reg-sub :schemes.form-handler/get-scheme-field        get-scheme-field)
(r/reg-sub :schemes.form-handler/get-scheme-field-ids    get-scheme-field-ids)
(r/reg-sub :schemes.form-handler/get-scheme-field-groups get-scheme-field-groups)
