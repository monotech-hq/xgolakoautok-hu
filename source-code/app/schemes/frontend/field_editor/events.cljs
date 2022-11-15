
(ns app.schemes.frontend.field-editor.events
    (:require [app.schemes.frontend.field-editor.subs :as field-editor.subs]
              [app.schemes.frontend.form-handler.subs :as form-handler.subs]
              [candy.api                              :refer [return]]
              [mid-fruits.map                         :refer [dissoc-in]]
              [mid-fruits.vector                      :as vector]
              [re-frame.api                           :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clean-data!
  ; @return (map)
  [db _]
  (-> db (dissoc-in [:schemes :field-editor/meta-items])
         (dissoc-in [:schemes :field-editor/field-item])
         (dissoc-in [:schemes :field-editor/backup-item])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-field!
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ scheme-id field-id]]
  (let [field-item (r form-handler.subs/get-scheme-field        db scheme-id field-id)
        field-name (r field-editor.subs/get-resolved-field-name db scheme-id field-id)]
       (-> db (assoc-in [:schemes :field-editor/field-item]
                        (assoc field-item :field/name field-name))
              (assoc-in [:schemes :field-editor/backup-item] field-item))))

(defn load-editor!
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  ;
  ; @return (map)
  [db [_ scheme-id field-id]]
  (cond-> db :clean-data! (as-> % (r clean-data! %))
             field-id     (as-> % (r load-field! % scheme-id field-id))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-field!
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  ; @param (namespaced map) field-item
  ;
  ; @return (map)
  [db [_ scheme-id field-id field-item]]
  (if field-id ; Ha a szerkesztő létező mező szerkesztése módban fut ...
               (letfn [(f [%] (if (= field-id (:field/field-id %))
                                  (return field-item)
                                  (return %)))]
                      (update-in db [:schemes :form-handler/scheme-forms scheme-id :scheme/fields] vector/->items f))
               ; Ha a szerkesztő új mező hozzáadása módban fut ...
               (update-in db [:schemes :form-handler/scheme-forms scheme-id :scheme/fields] vector/conj-item field-item)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-failed
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  ;
  ; @return (map)
  [db [_ _ _]]
  (assoc-in db [:schemes :field-editor/meta-items :editor-status] :save-failed))

(defn field-saving
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  ;
  ; @return (map)
  [db [_ _ _]]
  (assoc-in db [:schemes :field-editor/meta-items :editor-status] :saving))

(defn save-successed
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ scheme-id field-id server-response]]
  (let [field-item ((symbol "schemes.field-editor/save-field!") server-response)]
       (as-> db % (r add-field!  % scheme-id field-id field-item)
                  (r clean-data! %))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-db :schemes.field-editor/save-failed    save-failed)
(r/reg-event-db :schemes.field-editor/save-successed save-successed)
