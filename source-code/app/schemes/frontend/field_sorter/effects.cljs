
(ns app.schemes.frontend.field-sorter.effects
    (:require [app.schemes.frontend.field-sorter.events  :as field-sorter.events]
              [app.schemes.frontend.field-sorter.queries :as field-sorter.queries]
              [app.schemes.frontend.field-sorter.views   :as field-sorter.views]
              [re-frame.api                              :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :schemes.field-sorter/render-sorter!
  ; @param (keyword) scheme-id
  (fn [_ [_ scheme-id]]
      [:x.ui/render-popup! :schemes.field-sorter/view
                           {:content [field-sorter.views/view scheme-id]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :schemes.field-sorter/reorder-fields!
  ; @param (keyword) scheme-id
  ; @param (namespaced maps in vector) reordered-fields
  (fn [{:keys [db]} [_ scheme-id reordered-fields]]
      (let [query (field-sorter.queries/get-reorder-fields-query scheme-id reordered-fields)]
           {:db       (r field-sorter.events/reorder-fields! db scheme-id reordered-fields)
            :dispatch [:pathom/send-query! :schemes.field-sorter/reorder-fields!
                                           {:display-progress? true
                                            :on-success        [:schemes.field-sorter/reorder-fields-successed scheme-id]
                                            :on-failure        [:schemes.field-sorter/reorder-fields-failured  scheme-id]
                                            :query             query}]})))

(r/reg-event-fx :schemes.field-sorter/reorder-fields-successed
  ; @param (keyword) scheme-id
  ; @param (map) server-response
  {})

(r/reg-event-fx :schemes.field-sorter/reorder-fields-failured
  ; @param (keyword) scheme-id
  ; @param (map) server-response
  {})
