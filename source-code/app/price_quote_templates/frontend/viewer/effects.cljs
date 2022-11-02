
(ns app.price-quote-templates.frontend.viewer.effects
    (:require [app.price-quote-templates.frontend.viewer.views :as viewer.views]
              [re-frame.api                                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :price-quote-templates.viewer/load-viewer!
  {:dispatch-n [[:gestures/init-view-handler! :price-quote-templates.viewer
                                              {:default-view-id :overview}]
                [:ui/render-surface! :price-quote-templates.viewer/view
                                     {:content #'viewer.views/view}]]})
