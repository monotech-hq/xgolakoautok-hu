
(ns app.views.frontend.blank-page.effects
    (:require [app.views.frontend.blank-page.events :as blank-page.events]
              [app.views.frontend.blank-page.views  :as blank-page.views]
              [re-frame.api                         :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :views.blank-page/load-page!
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ page-id page-props]]
      {:db             (r blank-page.events/load-page! db)
       :dispatch-n     [[:ui/simulate-process!]
                        [:views.blank-page/render-page! page-id page-props]]
       :dispatch-later [{:ms 500 :dispatch [:views.blank-page/page-loaded]}]}))

(r/reg-event-fx :views.blank-page/render-page!
  (fn [_ [_ page-id page-props]]
      [:ui/render-surface! page-id
                           {:content [blank-page.views/view page-id page-props]}]))
