
(ns app.website-menus.frontend.viewer.effects
    (:require [app.website-menus.frontend.viewer.views :as viewer.views]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :website-menus.viewer/load-viewer!
  {:dispatch-n [[:x.gestures/init-view-handler! :website-menus.viewer
                                                {:default-view-id :overview}]
                [:website-menus.viewer/render-viewer!]]})

(r/reg-event-fx :website-menus.viewer/render-viewer!
  [:x.ui/render-surface! :website-menus.viewer/view
                         {:content #'viewer.views/view}])
