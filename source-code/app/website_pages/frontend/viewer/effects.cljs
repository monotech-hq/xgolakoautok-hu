
(ns app.website-pages.frontend.viewer.effects
    (:require [app.website-pages.frontend.viewer.views :as viewer.views]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :website-pages.viewer/load-viewer!
  {:dispatch-n [[:x.gestures/init-view-handler! :website-pages.viewer
                                                {:default-view-id :overview}]
                [:website-pages.viewer/render-viewer!]]})

(r/reg-event-fx :website-pages.viewer/render-viewer!
  [:x.ui/render-surface! :website-pages.viewer/view
                         {:content #'viewer.views/view}])
