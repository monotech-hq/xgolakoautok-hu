
(ns app.rental-vehicles.frontend.viewer.effects
    (:require [app.rental-vehicles.frontend.viewer.views :as viewer.views]
              [re-frame.api                              :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :rental-vehicles.viewer/load-viewer!
  {:dispatch-n [[:x.gestures/init-view-handler! :rental-vehicles.viewer
                                                {:default-view-id :overview}]
                [:rental-vehicles.viewer/render-viewer!]]})

(r/reg-event-fx :rental-vehicles.viewer/render-viewer!
  [:x.ui/render-surface! :rental-vehicles.viewer/view
                         {:content #'viewer.views/view}])
