
(ns app.price-quotes.frontend.viewer.effects
    (:require [app.price-quotes.frontend.viewer.views :as viewer.views]
              [re-frame.api                           :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :price-quotes.viewer/load-viewer!
  {:dispatch-n [[:gestures/init-view-handler! :price-quotes.viewer
                                              {:default-view-id :overview}]
                [:ui/render-surface! :price-quotes.viewer/view
                                     {:content #'viewer.views/view}]]})
