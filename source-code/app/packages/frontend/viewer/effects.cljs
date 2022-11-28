
(ns app.packages.frontend.viewer.effects
    (:require [app.packages.frontend.viewer.views :as viewer.views]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :packages.viewer/load-viewer!
  ; @param (keyword) view-id
  (fn [_ [_ view-id]]
      {:dispatch-n [[:x.gestures/init-view-handler! :packages.viewer
                                                    {:default-view-id (or view-id :overview)}]
                    [:packages.viewer/render-viewer!]]}))

(r/reg-event-fx :packages.viewer/render-viewer!
  [:x.ui/render-surface! :packages.viewer/view
                         {:content #'viewer.views/view}])
