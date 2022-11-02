
(ns app.pages.frontend.viewer.effects
    (:require [app.pages.frontend.viewer.views :as viewer.views]
              [re-frame.api                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pages.viewer/load-viewer!
  {:dispatch-n [[:gestures/init-view-handler! :pages.viewer
                                              {:default-view-id :overview}]
                [:ui/render-surface! :pages.viewer/view
                                     {:content #'viewer.views/view}]]})
