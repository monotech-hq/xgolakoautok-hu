
(ns app.schemes.frontend.field-editor.effects
    (:require [app.schemes.frontend.field-editor.events  :as field-editor.events]
              [app.schemes.frontend.field-editor.queries :as field-editor.queries]
              [app.schemes.frontend.field-editor.views   :as field-editor.views]
              [re-frame.api                              :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :schemes.field-editor/load-editor!
  ; @param (keyword) scheme-id
  ; @param (keyword)(opt) field-id
  ;
  ; @usage
  ;  [:schemes.field-editor/load-editor! :my-scheme]
  ;  =>
  ;  Starts the field-editor in {:new-field? true} mode.
  ;
  ; @usage
  ;  [:schemes.field-editor/load-editor! :my-scheme :my-field]
  ;  =>
  ;  Starts the field-editor in {:new-field? false} mode.
  (fn [{:keys [db]} [_ scheme-id field-id]]
      {:db       (r field-editor.events/load-editor! db scheme-id field-id)
       :dispatch [:x.ui/render-popup! :schemes.field-editor/view
                                      {:content [field-editor.views/view scheme-id field-id]}]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :schemes.field-editor/save-field!
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  (fn [{:keys [db]} [_ scheme-id field-id]]
      (let [query (r field-editor.queries/get-save-field-query db scheme-id field-id)]
           {:db       (r field-editor.events/field-saving db scheme-id field-id)
            :dispatch [:pathom/send-query! :schemes.field-editor/save-field!
                                           {:display-progress? true
                                            :on-success        [:schemes.field-editor/save-field-successed scheme-id field-id]
                                            :on-failure        [:schemes.field-editor/save-field-failured  scheme-id field-id]
                                            :query             query}]})))

(r/reg-event-fx :schemes.field-editor/save-field-successed
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  ; @param (map) server-response
  (fn [_ [_ scheme-id field-id server-response]]
      {:dispatch-later [{:ms  500 :dispatch [:x.ui/remove-popup! :schemes.field-editor/view]}
                        {:ms 1000 :dispatch [:schemes.field-editor/save-successed scheme-id field-id server-response]}]}))

(r/reg-event-fx :schemes.field-editor/save-field-failured
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ scheme-id field-id _]]
      {:dispatch-later [{:ms 1000 :dispatch [:schemes.field-editor/save-failed scheme-id field-id]}]}))
