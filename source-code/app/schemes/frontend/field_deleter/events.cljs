
(ns app.schemes.frontend.field-deleter.events
    (:require [mid-fruits.candy  :refer [return]]
              [mid-fruits.map    :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [re-frame.api      :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clean-data!
  ; @return (map)
  [db _]
  (dissoc-in db [:schemes :field-deleter/meta-items]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-field!
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ scheme-id field-id]]
  (letfn [(f [%] (not= field-id (:field/field-id %)))]
         (update-in db [:schemes :form-handler/scheme-forms scheme-id :scheme/fields] vector/filter-items f)))

(defn disable-field!
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ scheme-id field-id]]
  (letfn [(f [%] (if (= field-id (:field/field-id %))
                     (assoc  % :field/disabled? true)
                     (return %)))]
         (update-in db [:schemes :form-handler/scheme-forms scheme-id :scheme/fields] vector/->items f)))

(defn enable-field!
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ scheme-id field-id]]
  (letfn [(f [%] (if (= field-id (:field/field-id %))
                     (dissoc % :field/disabled?)
                     (return %)))]
         (update-in db [:schemes :form-handler/scheme-forms scheme-id :scheme/fields] vector/->items f)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-deleting
  ; @return (map)
  [db _]
  (assoc-in db [:schemes :field-deleter/meta-items :deleter-status] :deleting))

(defn delete-failed
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db _]
  (assoc-in db [:schemes :field-deleter/meta-items :deleter-status] :delete-failed))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-db :schemes.field-deleter/clean-data!   clean-data!)
(r/reg-event-db :schemes.field-deleter/remove-field! remove-field!)
(r/reg-event-db :schemes.field-deleter/delete-failed delete-failed)
