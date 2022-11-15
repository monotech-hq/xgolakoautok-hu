
(ns app.schemes.frontend.field-deleter.effects
    (:require [app.schemes.frontend.field-deleter.events  :as field-deleter.events]
              [app.schemes.frontend.field-deleter.queries :as field-deleter.queries]
              [app.schemes.frontend.field-deleter.views   :as field-deleter.views]
              [re-frame.api                               :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :schemes.field-deleter/delete-field!
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  (fn [{:keys [db]} [_ scheme-id field-id]]
      (let [query (r field-deleter.queries/get-delete-field-query db scheme-id field-id)]
           {:db       (r field-deleter.events/field-deleting db scheme-id field-id)
            :dispatch [:pathom/send-query! :schemes.field-deleter/delete-field!
                                           {:display-progress? true
                                            :on-failure [:schemes.field-deleter/delete-field-failured  scheme-id field-id]
                                            :on-success [:schemes.field-deleter/delete-field-successed scheme-id field-id]
                                            :query query}]})))

(r/reg-event-fx :schemes.field-deleter/delete-field-failured
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ; @param (map) server-response
  {:dispatch-later [{:ms 1000 :dispatch [:schemes.field-deleter/delete-failed]}
                    {:ms 3000 :dispatch [:x.ui/remove-popup! :schemes.field-deleter/consent]}
                    {:ms 3500 :dispatch [:schemes.field-deleter/clean-data!]}]})

(r/reg-event-fx :schemes.field-deleter/delete-field-successed
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ; @param (map) server-response
  (fn [_ [_ scheme-id field-id _]]
      {:dispatch-later [{:ms 1000 :dispatch [:x.ui/remove-popup! :schemes.field-deleter/consent]}
                        {:ms 1500 :dispatch [:schemes.field-deleter/clean-data!]}
                        {:ms 1500 :dispatch [:schemes.field-deleter/remove-field! scheme-id field-id]}]}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :schemes.field-deleter/render-consent!
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  (fn [{:keys [db]} [_ scheme-id field-id]]
      [:x.ui/render-popup! :schemes.field-deleter/consent
                           {:content [field-deleter.views/view scheme-id field-id]}]))
