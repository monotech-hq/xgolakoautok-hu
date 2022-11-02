
(ns app.models.frontend.viewer.effects
    (:require [app.models.frontend.viewer.views :as viewer.views]
              [re-frame.api                     :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :models.viewer/load-viewer!
  ; @param (keyword) view-id
  (fn [_ [_ view-id]]
      {:dispatch-n [[:gestures/init-view-handler! :models.viewer
                                                  {:default-view-id (or view-id :overview)}]
                    [:ui/render-surface! :models.viewer/view
                                         {:content #'viewer.views/view}]]}))
