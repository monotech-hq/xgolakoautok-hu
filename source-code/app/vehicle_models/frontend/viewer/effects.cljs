
(ns app.vehicle-models.frontend.viewer.effects
    (:require [app.vehicle-models.frontend.viewer.views :as viewer.views]
              [re-frame.api                             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :vehicle-models.viewer/load-viewer!
  ; @param (keyword) view-id
  (fn [_ [_ view-id]]
      {:dispatch-n [[:x.gestures/init-view-handler! :vehicle-models.viewer
                                                    {:default-view-id (or view-id :overview)}]
                    [:vehicle-models.viewer/render-viewer!]]}))

(r/reg-event-fx :vehicle-models.viewer/render-viewer!
  [:x.ui/render-surface! :vehicle-models.viewer/view
                         {:content #'viewer.views/view}])
