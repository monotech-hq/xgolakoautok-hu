
(ns app.vehicle-types.frontend.viewer.effects
  (:require [app.vehicle-types.frontend.viewer.views :as viewer.views]
            [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :vehicle-types.viewer/load-viewer!
  {:dispatch-n [[:x.gestures/init-view-handler! :vehicle-types.viewer
                                                {:default-view-id :overview}]
                [:vehicle-types.viewer/render-viewer!]]})

(r/reg-event-fx :vehicle-types.viewer/render-viewer!
  [:x.ui/render-surface! :vehicle-types.viewer/view
                         {:content #'viewer.views/view}])
