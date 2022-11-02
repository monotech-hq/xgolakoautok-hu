
(ns app.contents.frontend.viewer.effects
    (:require [app.contents.frontend.viewer.views :as viewer.views]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :contents.viewer/load-viewer!
  {:dispatch-n [[:gestures/init-view-handler! :contents.viewer
                                              {:default-view-id :overview}]
                [:ui/render-surface! :contents.viewer/view
                                     {:content #'viewer.views/view}]]})
