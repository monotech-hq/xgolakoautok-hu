
(ns app.types.frontend.viewer.effects
  (:require [app.types.frontend.viewer.views :as viewer.views]
            [re-frame.api                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :types.viewer/load-viewer!
  {:dispatch-n [[:gestures/init-view-handler! :types.viewer
                                              {:default-view-id :overview}]
                [:ui/render-surface! :types.viewer/view
                                     {:content #'viewer.views/view}]]})
