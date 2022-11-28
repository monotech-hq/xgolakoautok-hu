
(ns app.products.frontend.viewer.effects
    (:require [app.products.frontend.viewer.views :as viewer.views]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :products.viewer/load-viewer!
  {:dispatch-n [[:x.gestures/init-view-handler! :products.viewer
                                                {:default-view-id :overview}]
                [:products.viewer/render-viewer!]]})

(r/reg-event-fx :products.viewer/render-viewer!
  [:x.ui/render-surface! :products.viewer/view
                         {:content #'viewer.views/view}])
