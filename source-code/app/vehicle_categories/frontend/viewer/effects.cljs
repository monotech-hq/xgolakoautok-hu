
(ns app.vehicle-categories.frontend.viewer.effects
    (:require [app.vehicle-categories.frontend.viewer.views :as viewer.views]
              [re-frame.api                                 :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :vehicle-categories.viewer/load-viewer!
  ; @param (keyword) view-id
  (fn [_ [_ view-id]]
      {:dispatch-n [[:x.gestures/init-view-handler! :vehicle-categories.viewer
                                                    {:default-view-id (or view-id :overview)}]
                    [:vehicle-categories.viewer/render-viewer!]]}))

(r/reg-event-fx :vehicle-categories.viewer/render-viewer!
  [:x.ui/render-surface! :vehicle-categories.viewer/view
                         {:content #'viewer.views/view}])
