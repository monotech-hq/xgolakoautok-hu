
(ns app.vehicle-models.frontend.lister.effects
    (:require [app.vehicle-models.frontend.lister.views :as lister.views]
              [re-frame.api                             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :vehicle-models.lister/load-lister!
  [:x.ui/render-surface! :vehicle-models.lister/view
                         {:content #'lister.views/view}])
