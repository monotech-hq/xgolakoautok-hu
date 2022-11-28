
(ns app.contents.frontend.viewer.effects
    (:require [app.contents.frontend.viewer.views :as viewer.views]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :contents.viewer/load-viewer!
  {:dispatch-n [[:x.gestures/init-view-handler! :contents.viewer
                                                {:default-view-id :overview}]
                [:contents.viewer/render-viewer!]]})

(r/reg-event-fx :contents.viewer/render-viewer!
  [:x.ui/render-surface! :contents.viewer/view
                         {:content #'viewer.views/view}])
